package com.app.examproject.controller;

import com.app.examproject.commons.errors.BusinessException;
import com.app.examproject.commons.errors.errors.ExamError;
import com.app.examproject.domains.dto.exams.*;
import com.app.examproject.services.ExamService;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
class ExamControllerTest {

    @Autowired
    WebApplicationContext context;

    @MockitoBean
    ExamService examService;

    MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();

    UUID userId;
    UUID examId;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        userId = UUID.randomUUID();
        examId = UUID.randomUUID();
    }

    private ExamResponse sampleExam() {
        return new ExamResponse(
                examId,
                "Exam",
                "Description",
                "S1",
                Instant.now(),
                List.of(
                        new QuestionResponse(
                                UUID.randomUUID(),
                                "Question 1",
                                "QCM",
                                1,
                                List.of(
                                        new AnswerResponse(
                                                UUID.randomUUID(),
                                                "Answer 1",
                                                true,
                                                1
                                        )
                                )
                        )
                )
        );
    }

    @Test
    void create_exam_should_return_201() throws Exception {
        CreateExamRequest request = new CreateExamRequest(
                "Exam",
                "Description",
                "S1",
                List.of()
        );

        when(examService.create(eq(userId), any()))
                .thenReturn(sampleExam());

        mockMvc.perform(post("/api/exams/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.examId").value(examId.toString()))
                .andExpect(jsonPath("$.data.title").value("Exam"));
    }

    @Test
    void get_all_exams_should_return_200() throws Exception {
        when(examService.getAll()).thenReturn(List.of());

        mockMvc.perform(get("/api/exams"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    void get_exam_by_id_should_return_200() throws Exception {
        when(examService.getById(examId))
                .thenReturn(sampleExam());

        mockMvc.perform(get("/api/exams/{id}", examId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.examId").value(examId.toString()))
                .andExpect(jsonPath("$.data.questions").isArray());
    }

    @Test
    void get_exam_by_id_should_return_404_when_not_found() throws Exception {
        when(examService.getById(examId))
                .thenThrow(new BusinessException(ExamError.EXAM_NOT_FOUND));

        mockMvc.perform(get("/api/exams/{id}", examId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("EXAM_404"))
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void delete_exam_should_return_204() throws Exception {
        mockMvc.perform(delete("/api/exams/{id}", examId))
                .andExpect(status().isNoContent());
    }

    @Test
    void delete_exam_should_return_404_when_not_found() throws Exception {
        doThrow(new BusinessException(ExamError.EXAM_NOT_FOUND))
                .when(examService).delete(examId);

        mockMvc.perform(delete("/api/exams/{id}", examId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("EXAM_404"));
    }
}
