package com.app.examproject.domains;

import com.app.examproject.domains.dto.correct.ExamCorrectionResponse;
import com.app.examproject.domains.entities.CorrectionAnswerEntity;
import com.app.examproject.domains.entities.ExamAttemptEntity;
import com.app.examproject.domains.entities.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.Instant;
import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = AnswerCorrectionMapper.class
)
public interface ExamCorrectionMapper {

    @Mapping(target = "attemptId", source = "attempt.examAttemptId")
    @Mapping(target = "correctedBy", source = "professor.userId")
    @Mapping(target = "correctedAt", source = "correctedAt")

    @Mapping(
            target = "totalQuestions",
            expression = "java(attempt.getExamSession().getExam().getQuestions().size())"
    )
    @Mapping(
            target = "correctAnswers",
            expression = "java((int) corrections.stream().filter(CorrectionAnswerEntity::isCorrect).count())"
    )
    @Mapping(target = "score", source = "finalScore")

    @Mapping(target = "questions", source = "corrections")
    ExamCorrectionResponse toResponse(
            ExamAttemptEntity attempt,
            List<CorrectionAnswerEntity> corrections,
            UserEntity professor,
            Instant correctedAt,
            double finalScore
    );
}
