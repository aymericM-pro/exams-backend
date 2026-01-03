package com.app.examproject.controller;

import com.app.examproject.TestFixtures;
import com.app.examproject.domains.dto.exams.CreateExamRequest;
import com.app.examproject.domains.dto.exams.UpdateExamRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
@ActiveProfiles("test")
class ExamControllerIT {

    @Autowired
    MockMvc mockMvc;

    private final ObjectMapper objectMapper =
            com.fasterxml.jackson.databind.json.JsonMapper.builder()
                    .findAndAddModules()
                    .build();


    @Test
    void create_exam_should_return_201() throws Exception {
        CreateExamRequest request = TestFixtures.createExamRequest();

        mockMvc.perform(post("/api/exams")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value("Exam"));
    }

    @Test
    void create_exam_should_return_400_when_invalid() throws Exception {
        CreateExamRequest request = new CreateExamRequest(
                "",
                "desc",
                "S1",
                java.util.List.of()
        );

        mockMvc.perform(post("/api/exams")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_REQUEST"));
    }

    @Test
    void get_exam_by_id_should_return_200() throws Exception {
        UUID id = createExamAndReturnId();

        mockMvc.perform(get("/api/exams/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()));
    }

    @Test
    void get_exam_by_id_should_return_404_when_not_found() throws Exception {
        mockMvc.perform(get("/api/exams/{id}", UUID.randomUUID()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("EXAM_NOT_FOUND"));
    }

    @Test
    void update_exam_should_return_200() throws Exception {
        UUID id = createExamAndReturnId();

        UpdateExamRequest request = TestFixtures.updateExamRequest();

        mockMvc.perform(put("/exams/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Exam"));
    }

    @Test
    void delete_exam_should_return_204() throws Exception {
        UUID id = createExamAndReturnId();

        mockMvc.perform(delete("/api/exams/{id}", id))
                .andExpect(status().isNoContent());
    }

    @Test
    void delete_exam_should_return_404_when_not_found() throws Exception {
        mockMvc.perform(delete("/api/exams/{id}", UUID.randomUUID()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("EXAM_NOT_FOUND"));
    }

    private UUID createExamAndReturnId() throws Exception {
        CreateExamRequest request = TestFixtures.createExamRequest();

        String response = mockMvc.perform(post("/api/exams")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return UUID.fromString(
                objectMapper.readTree(response).get("id").asText()
        );
    }
}
