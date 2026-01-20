package com.app.examproject.controller;

import com.app.examproject.commons.errors.BusinessException;
import com.app.examproject.commons.errors.GlobalExceptionHandler;
import com.app.examproject.commons.errors.errors.UserError;
import com.app.examproject.controller.users.UserController;
import com.app.examproject.domains.dto.users.CreateUserRequest;
import com.app.examproject.domains.dto.users.UpdateUserRequest;
import com.app.examproject.domains.dto.users.UserResponse;
import com.app.examproject.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@Import(GlobalExceptionHandler.class)
@ActiveProfiles("test")
class UserControllerIT {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    UserService userService;

    // ✅ ObjectMapper LOCAL, pas Spring-managed
    private final ObjectMapper objectMapper = new ObjectMapper();

    /* ===================== CREATE ===================== */

    @Test
    void createUserReturns201() throws Exception {
        UserResponse response = UserResponse.builder()
                .userId(UUID.randomUUID())
                .email("john_doe@gmail.com")
                .firstname("John")
                .lastname("Doe")
                .role("ROLE_STUDENT")
                .build();

        when(userService.create(any())).thenReturn(response);

        CreateUserRequest request = new CreateUserRequest(
                "john_doe@gmail.com",
                "John",
                "Doe",
                "ROLE_STUDENT"
        );

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.userId").value(response.getUserId().toString()))
                .andExpect(jsonPath("$.data.email").value("john_doe@gmail.com"))
                .andExpect(jsonPath("$.data.firstname").value("John"))
                .andExpect(jsonPath("$.data.lastname").value("Doe"))
                .andExpect(jsonPath("$.data.role").value("ROLE_STUDENT"));
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
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_REQUEST"))
                .andExpect(jsonPath("$.status").value(400));
    }

    /* ===================== GET ===================== */

    @Test
    void getAllUsersReturns200() throws Exception {
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk());
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

    /* ===================== UPDATE ===================== */

    @Test
    void updateUserByIdReturns200() throws Exception {
        UUID id = UUID.randomUUID();

        UserResponse response = UserResponse.builder()
                .userId(id)
                .email("john_doe@gmail.com")
                .firstname("John")
                .lastname("Doe")
                .role("ROLE_STUDENT")
                .build();

        when(userService.update(eq(id), any())).thenReturn(response);

        UpdateUserRequest request = new UpdateUserRequest(
                "john_doe@gmail.com",
                "John",
                "Doe",
                "ROLE_STUDENT"
        );

        mockMvc.perform(put("/api/users/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.userId").value(id.toString()));
    }

    @Test
    void updateUserReturns404WhenNotFound() throws Exception {
        UUID id = UUID.randomUUID();

        when(userService.update(eq(id), any()))
                .thenThrow(new BusinessException(UserError.USER_NOT_FOUND));

        UpdateUserRequest request = new UpdateUserRequest(
                "john_doe@gmail.com",
                "John",
                "Doe",
                "ROLE_STUDENT"
        );

        mockMvc.perform(put("/api/users/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("USER_404"));
    }

    /* ===================== DELETE ===================== */

    @Test
    void deleteUserByIdReturns204() throws Exception {
        mockMvc.perform(delete("/api/users/{id}", UUID.randomUUID()))
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

    /* ===================== SEARCH ===================== */

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
