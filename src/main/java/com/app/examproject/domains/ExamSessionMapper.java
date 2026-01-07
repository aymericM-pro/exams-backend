package com.app.examproject.domains;

import com.app.examproject.domains.dto.exams_sessions.CreateExamSessionRequest;
import com.app.examproject.domains.dto.exams_sessions.ExamSessionResponse;
import com.app.examproject.domains.dto.exams_sessions.UpdateExamSessionRequest;
import com.app.examproject.domains.entities.ClassEntity;
import com.app.examproject.domains.entities.ExamEntity;
import com.app.examproject.domains.entities.sessions.ExamSessionEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ExamSessionMapper {

    /* =========================
       CREATE
    ========================== */

    @Mapping(target = "examSessionId", ignore = true)
    @Mapping(target = "exam", source = "exam")
    @Mapping(target = "studentClass", source = "studentClass")
    @Mapping(target = "status", constant = "PLANNED")
    @Mapping(target = "examAttempts", ignore = true)
    ExamSessionEntity toEntity(
            CreateExamSessionRequest request,
            ExamEntity exam,
            ClassEntity studentClass
    );

    /* =========================
       UPDATE (PARTIAL)
    ========================== */

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "examSessionId", ignore = true)
    @Mapping(target = "exam", ignore = true)
    @Mapping(target = "studentClass", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "examAttempts", ignore = true)
    void updateEntity(
            @MappingTarget ExamSessionEntity entity,
            UpdateExamSessionRequest request
    );

    /* =========================
       READ
    ========================== */

    @Mapping(target = "examSessionId", source = "examSessionId")
    @Mapping(target = "examId", source = "exam.examId")
    @Mapping(target = "examTitle", source = "exam.title")
    @Mapping(target = "classId", source = "studentClass.classId")
    @Mapping(target = "className", source = "studentClass.name")
    @Mapping(target = "attemptsCount",
            expression = "java(entity.getExamAttempts() == null ? 0 : entity.getExamAttempts().size())")
    ExamSessionResponse toResponse(ExamSessionEntity entity);
}
