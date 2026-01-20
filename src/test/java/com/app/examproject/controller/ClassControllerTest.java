package com.app.examproject.controller;

import com.app.examproject.domains.dto.classes.ClassResponse;
import com.app.examproject.domains.dto.classes.CreateClassRequest;
import com.app.examproject.services.ClassService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class ClassControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    ClassService classService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldCreateClassReturns201() throws Exception {
        UUID studentId = UUID.randomUUID();
        UUID professorId = UUID.randomUUID();

        CreateClassRequest request = new CreateClassRequest(
                "Class A",
                "2025",
                List.of(studentId),
                List.of(professorId)
        );

        ClassResponse response = new ClassResponse(
                UUID.randomUUID(),
                "Class A",
                "2025",
                List.of(studentId),
                List.of(professorId)
        );

        when(classService.create(any())).thenReturn(response);

        mockMvc.perform(post("/api/classes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.classId").value(response.classId().toString()))
                .andExpect(jsonPath("$.data.name").value("Class A"))
                .andExpect(jsonPath("$.data.graduationYear").value("2025"))
                .andExpect(jsonPath("$.data.studentIds[0]").value(studentId.toString()))
                .andExpect(jsonPath("$.data.professorIds[0]").value(professorId.toString()));
    }
}
