package com.perficient.sbapptsystem.web.client;

import com.perficient.sbapptsystem.web.model.ApptDto;
import com.perficient.sbapptsystem.web.model.UserDto;
import org.apache.catalina.User;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

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

    public void setApiHost(String apiHost) {
        this.apiHost = apiHost;
    }
}