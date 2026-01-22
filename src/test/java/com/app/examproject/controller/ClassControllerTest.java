package com.app.examproject.controller;

import com.app.examproject.commons.errors.BusinessException;
import com.app.examproject.commons.errors.errors.ClassError;
import com.app.examproject.domains.dto.classes.ClassResponse;
import com.app.examproject.domains.dto.classes.CreateClassRequest;
import com.app.examproject.domains.dto.users.StudentResponse;
import com.app.examproject.services.ClassService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    void getClassReturns404WhenClassNotFound() throws Exception {
        UUID classId = UUID.randomUUID();
        when(classService.getById(classId)).thenThrow(new BusinessException(ClassError.CLASS_NOT_FOUND));

        mockMvc.perform(get("/api/classes/{id}", classId))
                .andExpect(status().isNotFound());
    }

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

    @Test
    void createClassReturns400WhenRequestIsInvalid() throws Exception {
        CreateClassRequest invalidRequest = new CreateClassRequest(
                "",
                null,
                List.of(),
                List.of()
        );

        when(classService.create(any()))
                .thenThrow(new BusinessException(ClassError.INVALID_REQUEST));

        mockMvc.perform(post("/api/classes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllClassesReturns200() throws Exception {
        UUID classId = UUID.randomUUID();
        UUID studentId = UUID.randomUUID();
        UUID professorId = UUID.randomUUID();

        ClassResponse response = new ClassResponse(
                classId,
                "Class B",
                "2024",
                List.of(studentId),
                List.of(professorId)
        );

        when(classService.getAll()).thenReturn(List.of(response));

        mockMvc.perform(get("/api/classes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].classId").value(classId.toString()))
                .andExpect(jsonPath("$.data[0].name").value("Class B"))
                .andExpect(jsonPath("$.data[0].graduationYear").value("2024"))
                .andExpect(jsonPath("$.data[0].studentIds[0]").value(studentId.toString()))
                .andExpect(jsonPath("$.data[0].professorIds[0]").value(professorId.toString()));
    }

    @Test
    void getClassByIdReturns200() throws Exception {
        UUID classId = UUID.randomUUID();
        UUID studentId = UUID.randomUUID();
        UUID professorId = UUID.randomUUID();

        ClassResponse response = new ClassResponse(
                classId,
                "Class C",
                "2023",
                List.of(studentId),
                List.of(professorId)
        );

        when(classService.getById(classId)).thenReturn(response);

        mockMvc.perform(get("/api/classes/{id}", classId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.classId").value(classId.toString()))
                .andExpect(jsonPath("$.data.name").value("Class C"))
                .andExpect(jsonPath("$.data.graduationYear").value("2023"))
                .andExpect(jsonPath("$.data.studentIds[0]").value(studentId.toString()))
                .andExpect(jsonPath("$.data.professorIds[0]").value(professorId.toString()));
    }

    @Test
    void deleteClassReturns204() throws Exception {
        UUID classId = UUID.randomUUID();

        mockMvc.perform(delete("/api/classes/{id}", classId))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteClassReturns404WhenClassNotFound() throws Exception {
        UUID classId = UUID.randomUUID();

        doThrow(new BusinessException(ClassError.CLASS_NOT_FOUND))
                .when(classService).delete(classId);

        mockMvc.perform(delete("/api/classes/{id}", classId))
                .andExpect(status().isNotFound());
    }
    @Test
    void getStudentsByClassReturns200() throws Exception {
        UUID classId = UUID.randomUUID();

        StudentResponse student1 = new StudentResponse(
                UUID.randomUUID(),
                "john",
                "doe",
                "student@gmail.com"
        );

        StudentResponse student2 = new StudentResponse(
                UUID.randomUUID(),
                "jane",
                "doe",
                "student1@gmail.com"
        );

        when(classService.getStudentsByClass(classId))
                .thenReturn(List.of(student1, student2));

        mockMvc.perform(get("/api/classes/{classId}/students", classId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id").value(student1.id().toString()))
                .andExpect(jsonPath("$.data[0].firstname").value("john"))
                .andExpect(jsonPath("$.data[0].lastname").value("doe"))
                .andExpect(jsonPath("$.data[0].email").value("student@gmail.com"))
                .andExpect(jsonPath("$.data[1].id").value(student2.id().toString()))
                .andExpect(jsonPath("$.data[1].firstname").value("jane"))
                .andExpect(jsonPath("$.data[1].lastname").value("doe"))
                .andExpect(jsonPath("$.data[1].email").value("student1@gmail.com"));
    }

    @Test
    void exportStudentPdfReturns200() throws Exception {
        UUID classId = UUID.randomUUID();

        byte[] pdfBytes = "fake-pdf-content".getBytes();

        when(classService.exportStudentsPdf(classId)).thenReturn(pdfBytes);

        mockMvc.perform(get("/api/classes/{id}/students/pdf", classId))
                .andExpect(status().isOk())
                .andExpect(content().bytes(pdfBytes))
                .andExpect(header().string(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=class-students.pdf"
                ))
                .andExpect(content().contentType(MediaType.APPLICATION_PDF))
                .andExpect(content().bytes(pdfBytes));
    }

    @Test
    void removeStudentFromClassReturns204() throws Exception {
        UUID classId = UUID.randomUUID();
        UUID studentId = UUID.randomUUID();

        mockMvc.perform(delete("/api/classes/{classId}/students/{studentId}", classId, studentId))
                .andExpect(status().isNoContent());

        verify(classService).removeStudentFromClass(classId, studentId);
    }

    @Test
    void getMyClassesReturns200() throws Exception {
        UUID teacherId = UUID.fromString("78c1263b-f1f4-42dd-9298-bc9852a23853");

        UUID classId = UUID.randomUUID();
        UUID studentId = UUID.randomUUID();

        ClassResponse response = new ClassResponse(
                classId,
                "My Class",
                "2025",
                List.of(studentId),
                List.of(teacherId)
        );

        when(classService.getClassesByTeacher(teacherId))
                .thenReturn(List.of(response));

        mockMvc.perform(get("/api/classes/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].classId").value(classId.toString()))
                .andExpect(jsonPath("$.data[0].name").value("My Class"))
                .andExpect(jsonPath("$.data[0].graduationYear").value("2025"))
                .andExpect(jsonPath("$.data[0].studentIds[0]").value(studentId.toString()))
                .andExpect(jsonPath("$.data[0].professorIds[0]").value(teacherId.toString()));

        verify(classService).getClassesByTeacher(teacherId);
    }
}
