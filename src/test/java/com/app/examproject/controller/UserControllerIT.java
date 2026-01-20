package com.app.examproject.controller;

import com.app.examproject.commons.errors.BusinessException;
import com.app.examproject.commons.errors.errors.UserError;
import com.app.examproject.domains.dto.users.CreateUserRequest;
import com.app.examproject.domains.dto.users.UpdateUserRequest;
import com.app.examproject.domains.dto.users.UserResponse;
import com.app.examproject.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import tools.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
class UserControllerIT {

    @Autowired
    WebApplicationContext context;

    MockMvc mockMvc;

    @MockitoBean
    UserService userService;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

        UserResponse response = UserResponse.builder()
                .userId(UUID.randomUUID())
                .email("john_doe@gmail.com")
                .firstname("John")
                .lastname("Doe")
                .role("ROLE_STUDENT")
                .build();

        when(userService.create(any())).thenReturn(response);
    }

    @Test
    void createUserReturns201() throws Exception {
        CreateUserRequest request = new CreateUserRequest(
                "john_doe@gmail.com",
                "John",
                "Doe",
                "ROLE_STUDENT"
        );

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.email").value("john_doe@gmail.com"))
                .andExpect(jsonPath("$.data.role").value("ROLE_STUDENT"));
    }

    @Test
    void getAllUsersReturns200() throws Exception {
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk());
    }

    @Test
    void createUserReturns400WhenEmailInvalid() throws Exception {
        CreateUserRequest request = new CreateUserRequest(
                "not-an-email",
                "John",
                "Doe",
                "ROLE_STUDENT"
        );

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_REQUEST"))
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void getUserByIdReturns200() throws Exception {
        UUID id = UUID.randomUUID();

        UserResponse response = UserResponse.builder()
                .userId(id)
                .email("john_doe@gmail.com")
                .firstname("John")
                .lastname("Doe")
                .role("ROLE_STUDENT")
                .build();

        when(userService.getById(id)).thenReturn(response);

        mockMvc.perform(get("/api/users/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.userId").value(id.toString()));
    }

    @Test
    void getUserByIdReturns404WhenNotFound() throws Exception {
        UUID id = UUID.randomUUID();

        when(userService.getById(id))
                .thenThrow(new BusinessException(UserError.USER_NOT_FOUND));

        mockMvc.perform(get("/api/users/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("USER_404"))
                .andExpect(jsonPath("$.error").value("User not found"))
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void updateUserByIdReturns200() throws Exception {
        UUID id = UUID.randomUUID();

        UpdateUserRequest request = new UpdateUserRequest(
                "john_doe@gmail.com",
                "John",
                "Doe",
                "ROLE_STUDENT"
        );

        UserResponse response = UserResponse.builder()
                .userId(id)
                .email("john_doe@gmail.com")
                .firstname("John")
                .lastname("Doe")
                .role("ROLE_STUDENT")
                .build();

        when(userService.update(eq(id), any()))
                .thenReturn(response);

        mockMvc.perform(put("/api/users/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.userId").value(id.toString()))
                .andExpect(jsonPath("$.data.email").value("john_doe@gmail.com"))
                .andExpect(jsonPath("$.data.firstname").value("John"))
                .andExpect(jsonPath("$.data.lastname").value("Doe"))
                .andExpect(jsonPath("$.data.role").value("ROLE_STUDENT"));
    }

    @Test
    void updateUserReturns404WhenNotFound() throws Exception {
        UUID id = UUID.randomUUID();

        UpdateUserRequest request = new UpdateUserRequest(
                "john_doe@gmail.com",
                "John",
                "Doe",
                "ROLE_STUDENT"
        );

        when(userService.update(eq(id), any()))
                .thenThrow(new BusinessException(UserError.USER_NOT_FOUND));

        mockMvc.perform(put("/api/users/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("USER_404"))
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void deleteUserByIdReturns204() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(delete("/api/users/{id}", id))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteUserReturns404WhenNotFound() throws Exception {
        UUID id = UUID.randomUUID();

        doThrow(new BusinessException(UserError.USER_NOT_FOUND))
                .when(userService).delete(id);

        mockMvc.perform(delete("/api/users/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("USER_404"));
    }

    @Test
    void searchUsersWithoutFiltersReturns200() throws Exception {
        mockMvc.perform(get("/api/users/search"))
                .andExpect(status().isOk());
    }

    @Test
    void searchUsersByFirstnameReturns200() throws Exception {
        mockMvc.perform(get("/api/users/search")
                        .param("firstname", "John"))
                .andExpect(status().isOk());
    }

    @Test
    void searchUsersByRoleReturns200() throws Exception {
        mockMvc.perform(get("/api/users/search")
                        .param("role", "STUDENT"))
                .andExpect(status().isOk());
    }
}
