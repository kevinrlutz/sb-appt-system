package com.perficient.sbapptsystem.web.client;

import com.perficient.sbapptsystem.web.model.ApptDto;
import com.perficient.sbapptsystem.web.model.ApptFormatter;
import com.perficient.sbapptsystem.web.model.UserDto;
import org.bson.types.ObjectId;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
@ConfigurationProperties(value = "app.admin", ignoreUnknownFields = false)
public class AdminClient {

    public final String USER_PATH = "/api/v1/users";
    private String apiHost;

    private final RestTemplate restTemplate;

    public AdminClient(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public List<UserDto> getAllUsers() {
        return restTemplate.getForObject("http://localhost:8080/api/v1/users", List.class);
    }

    public List<ApptDto> getAllAppts() {
        return restTemplate.getForObject("http://localhost:8081/appointments", List.class);
    }

    public UserDto getUserById(String userId) {
        return restTemplate.getForObject("http://localhost:8080/api/v1/users/" + userId, UserDto.class);
    }

    public ApptDto getApptById(String apptId) {
        return restTemplate.getForObject("http://localhost:8081/appointments/" + apptId, ApptDto.class);
    }

    public List<UserDto> findByLastName(String lastName) {
        return restTemplate.getForObject("http://localhost:8080/api/v1/users/search/" + lastName, List.class);
    }

    public List<ApptDto> findByApptName(String apptName) {
        System.out.println("In findByApptName client method");
        return restTemplate.getForObject("http://localhost:8081/appointments/search/" + apptName, List.class);
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

        restTemplate.put("http://localhost:8080/api/v1/users/" + userId, updatedUser);
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

        return restTemplate.postForObject("http://localhost:8080/api/v1/users/", saveUser, UserDto.class);
    }

    public ApptDto createAppt(@RequestBody ApptFormatter apptFormatter, UserDto user) {

        String startTime = apptFormatter.getStartTime();
        String endTime = apptFormatter.getEndTime();
        // System.out.println(startTime + " --- " + endTime);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime formattedStartTime = LocalDateTime.parse(startTime, formatter);
        LocalDateTime formattedEndTime = LocalDateTime.parse(endTime, formatter);

        System.out.println("In AdminClient 1");

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

        System.out.println(saveAppt);
        user.getAppointmentList().add(saveAppt);
        updateUser(user.getId(), user);

        System.out.println("In AdminClient 2");

        return restTemplate.postForObject("http://localhost:8081/appointments/", saveAppt, ApptDto.class);
    }

    public void deleteUser(String userId) {
        restTemplate.delete("http://localhost:8080/api/v1/users/" + userId);
    }

    public void deleteAppt(String userId, String apptId) {
        UserDto user = getUserById(userId);
        ApptDto deleteAppt = getApptById(apptId);

        user.getAppointmentList().remove(deleteAppt);
        updateUser(user.getId(), user);

        restTemplate.delete("http://localhost:8081/appointments/" + apptId);
    }

    public void setApiHost(String apiHost) {
        this.apiHost = apiHost;
    }

    public void updateAppt(String apptId, @RequestBody ApptFormatter apptFormatter) {

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

        restTemplate.put("http://localhost:8081/appointments/" + apptId, updateAppt);
    }

}
