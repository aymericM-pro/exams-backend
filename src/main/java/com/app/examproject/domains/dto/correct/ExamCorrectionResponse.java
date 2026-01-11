package com.app.examproject.domains.dto.correct;


import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ExamCorrectionResponse(
        UUID attemptId,
        UUID correctedBy,
        Instant correctedAt,

        int totalQuestions,
        int correctAnswers,
        double score,

        List<QuestionCorrectionResponse> questions
) {}