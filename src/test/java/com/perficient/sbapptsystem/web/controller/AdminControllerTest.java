package com.perficient.sbapptsystem.web.controller;

import com.perficient.sbapptsystem.web.client.AdminClient;
import com.perficient.sbapptsystem.web.model.ApptDto;
import com.perficient.sbapptsystem.web.model.ApptTypeEnum;
import com.perficient.sbapptsystem.web.model.UserDto;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import(AdminClient.class)
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    AdminClient adminClient;

    @Test
    @WithMockUser(username = "user", password = "springboot", roles = "USER")
    void login() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


    @Test
    @WithMockUser(username = "user", password = "springboot", roles = "USER")
    void index() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/home")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user", password = "springboot", roles = "USER")
    void users() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user", password = "springboot", roles = "USER")
    void appointments() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/appointments")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    UserDto getValidUserDto() {
        return UserDto.builder()
                .id(new ObjectId().toString())
                .firstName("Joe")
                .lastName("Bloggs")
                .gender("Male")
                .age(18)
                .email("test@test.com")
                .phoneNumber("555-555-5555")
                .appointmentList(new ArrayList<>())
                .build();
    }
}