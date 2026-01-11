package com.app.examproject.domains;

import com.app.examproject.domains.dto.correct.*;
import com.app.examproject.domains.entities.CorrectionAnswerEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AnswerCorrectionMapper {

    @Mapping(target = "correctAnswerId", ignore = true)
    @Mapping(target = "studentAnswer", ignore = true)
    @Mapping(target = "correctedBy", ignore = true)
    @Mapping(target = "correctedAt", expression = "java(java.time.Instant.now())")
    CorrectionAnswerEntity fromQuestionCorrection(
            QuestionCorrectionRequest request
    );

    @Mapping(target = "questionId", source = "studentAnswer.question.questionId")
    @Mapping(target = "finalCorrect", source = "correct")
    QuestionCorrectionResponse toQuestionResponse(
            CorrectionAnswerEntity entity
    );
}
