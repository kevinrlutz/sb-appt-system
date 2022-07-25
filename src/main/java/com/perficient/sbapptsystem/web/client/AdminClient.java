package com.perficient.sbapptsystem.web.client;

import com.perficient.sbapptsystem.web.model.ApptDto;
import com.perficient.sbapptsystem.web.model.ApptFormatter;
import com.perficient.sbapptsystem.web.model.UserDto;
import org.bson.types.ObjectId;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class AdminClient {

    public final String USER_PATH = "/api/v1/users/";

    private final String userServiceUrl = "http://localhost:8080";

    private final String apptServiceUrl = "http://localhost:8081";

    private final RestTemplate restTemplate;

    public AdminClient(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public List<UserDto> getAllUsers() {
        return restTemplate.getForObject(userServiceUrl + USER_PATH, List.class);
    }

    public List<ApptDto> getAllAppts() {
        return restTemplate.getForObject(apptServiceUrl + "/appointments/", List.class);
    }

    public UserDto getUserById(String userId) {
        return restTemplate.getForObject(userServiceUrl + USER_PATH + userId, UserDto.class);
    }

    public List<ApptDto> getUserAppointments(String userId) {
        return restTemplate.getForObject(apptServiceUrl + "/" + userId + "/appointments", List.class);
    }

    public ApptDto getApptById(String apptId) {
        return restTemplate.getForObject(apptServiceUrl + "/appointments/" + apptId, ApptDto.class);
    }

    public List<UserDto> findByLastName(String lastName) {
        return restTemplate.getForObject(userServiceUrl + USER_PATH + "search/" + lastName, List.class);
    }

    public List<ApptDto> findByApptName(String apptName) {
        return restTemplate.getForObject(apptServiceUrl + "/appointments/search/" + apptName, List.class);
    }

    public void updateUser(String userId, @RequestBody UserDto user) {
        UserDto updatedUser = UserDto.builder()
                .id(userId)
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .age(user.getAge())
                .gender(user.getGender())
                .appointmentList(user.getAppointmentList())
                .build();

        restTemplate.put(userServiceUrl + USER_PATH + userId, updatedUser);
    }

    public UserDto createUser(@RequestBody UserDto user) {
        UserDto saveUser = UserDto.builder()
                .id(new ObjectId().toString())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .age(user.getAge())
                .gender(user.getGender())
                .phoneNumber(user.getPhoneNumber())
                .email(user.getEmail())
                .appointmentList(new ArrayList<>())
                .build();

        return restTemplate.postForObject(userServiceUrl + USER_PATH, saveUser, UserDto.class);
    }

    public ApptDto createAppt(@RequestBody ApptFormatter apptFormatter, UserDto user) {

        String startTime = apptFormatter.getStartTime();
        String endTime = apptFormatter.getEndTime();
        // System.out.println(startTime + " --- " + endTime);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime formattedStartTime = LocalDateTime.parse(startTime, formatter);
        LocalDateTime formattedEndTime = LocalDateTime.parse(endTime, formatter);

        ApptDto saveAppt = ApptDto.builder()
                .id(new ObjectId().toString())
                .apptName(apptFormatter.getApptName())
                .apptType(apptFormatter.getApptType())
                .description(apptFormatter.getDescription())
                .startTime(formattedStartTime)
                .endTime(formattedEndTime)
                .metadata(apptFormatter.getMetadata())
                .userId(user.getId())
                .build();

        user.getAppointmentList().add(saveAppt);
        updateUser(user.getId(), user);

        return restTemplate.postForObject(apptServiceUrl + "/appointments/", saveAppt, ApptDto.class);
    }

    public void deleteUser(String userId) {
        UserDto user = getUserById(userId);
        user.getAppointmentList().stream().forEach(appt -> deleteAppt(userId, appt.getId()));

        restTemplate.delete(userServiceUrl + USER_PATH + userId);
    }

    public void deleteAppt(String userId, String apptId) {
        UserDto user = getUserById(userId);
        ApptDto deleteAppt = getApptById(apptId);

        user.getAppointmentList().remove(deleteAppt);
        updateUser(user.getId(), user);

        restTemplate.delete(apptServiceUrl + "/appointments/" + apptId);
    }

    public void updateAppt(String apptId, @RequestBody ApptFormatter apptFormatter, UserDto user) {
        ApptDto deleteAppt = getApptById(apptId);

        String startTime = apptFormatter.getStartTime();
        String endTime = apptFormatter.getEndTime();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime formattedStartTime = LocalDateTime.parse(startTime, formatter);
        LocalDateTime formattedEndTime = LocalDateTime.parse(endTime, formatter);

        ApptDto updateAppt = ApptDto.builder()
                .id(apptId)
                .apptName(apptFormatter.getApptName())
                .apptType(apptFormatter.getApptType())
                .description(apptFormatter.getDescription())
                .startTime(formattedStartTime)
                .endTime(formattedEndTime)
                .metadata(apptFormatter.getMetadata())
                .userId(apptFormatter.getUserId())
                .build();

        user.getAppointmentList().remove(deleteAppt);
        user.getAppointmentList().add(updateAppt);
        updateUser(user.getId(), user);

        restTemplate.put(apptServiceUrl + "/appointments/" + apptId, updateAppt);
    }

}
