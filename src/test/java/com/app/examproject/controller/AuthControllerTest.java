package com.app.examproject.controller;

import com.app.examproject.controller.auth.RegisterRequest;
import com.app.examproject.domains.UserMapper;
import com.app.examproject.domains.dto.users.UserResponse;
import com.app.examproject.services.AuthService;
import com.app.examproject.services.CurrentUserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    AuthService authService;

    @MockitoBean
    CurrentUserService currentUserService;

    @MockitoBean
    UserMapper userMapper;

    // ===================== REGISTER =====================

    @Test
    void shouldRegisterUserReturns201() throws Exception {
        RegisterRequest userRequest = new RegisterRequest(
                "john_doe@gmail.com",
                "securePassword123",
                "John",
                "Doe",
                "ROLE_STUDENT"
        );

        UserResponse userResponse = UserResponse.builder()
                .userId(UUID.randomUUID())
                .email("john_doe@gmail.com")
                .firstname("John")
                .lastname("Doe")
                .role("ROLE_STUDENT")
                .build();

        when(authService.register(userRequest)).thenReturn(userResponse);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.userId")
                        .value(userResponse.getUserId().toString()))
                .andExpect(jsonPath("$.data.email").value("john_doe@gmail.com"))
                .andExpect(jsonPath("$.data.firstname").value("John"))
                .andExpect(jsonPath("$.data.lastname").value("Doe"))
                .andExpect(jsonPath("$.data.role").value("ROLE_STUDENT"));
    }

}