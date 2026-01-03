package com.app.examproject.domains;

import com.app.examproject.domains.dto.exams.CreateExamRequest;
import com.app.examproject.domains.dto.exams.ExamResponse;
import com.app.examproject.domains.dto.exams.UpdateExamRequest;
import com.app.examproject.domains.entities.ExamEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        componentModel = "spring",
        uses = QuestionMapper.class
)
public interface ExamMapper {

    ExamResponse toResponse(ExamEntity entity);

    @Mapping(target = "examId", ignore = true)
    @Mapping(target = "createdAt", expression = "java(java.time.Instant.now())")
    ExamEntity fromCreate(CreateExamRequest request);

    @Mapping(target = "examId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    ExamEntity fromUpdate(UpdateExamRequest request);
}