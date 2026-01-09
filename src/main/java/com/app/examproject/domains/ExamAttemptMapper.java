package com.app.examproject.domains;


import com.app.examproject.domains.dto.exam_attempt.ExamAttemptResponse;
import com.app.examproject.domains.dto.exam_attempt.UpdateExamAttemptRequest;
import com.app.examproject.domains.entities.ExamAttemptEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ExamAttemptMapper {

    @Mapping(source = "examSession.examSessionId", target = "examSessionId")
    @Mapping(source = "student.userId", target = "studentId")
    ExamAttemptResponse toResponse(ExamAttemptEntity entity);

    @Mapping(target = "examAttemptId", ignore = true)
    @Mapping(target = "examSession", ignore = true)
    @Mapping(target = "student", ignore = true)
    @Mapping(target = "answers", ignore = true)
    @Mapping(target = "startedAt", ignore = true)
    ExamAttemptEntity toEntity(UpdateExamAttemptRequest request);

    @Mapping(target = "examAttemptId", ignore = true)
    @Mapping(target = "examSession", ignore = true)
    @Mapping(target = "student", ignore = true)
    @Mapping(target = "answers", ignore = true)
    @Mapping(target = "startedAt", ignore = true)
    void update(UpdateExamAttemptRequest request, @MappingTarget ExamAttemptEntity entity);
}
