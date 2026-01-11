package com.app.examproject.domains.dto.correct;

import java.util.UUID;

public record QuestionCorrectionResponse(
        UUID questionId,
        Boolean finalCorrect,
        Integer awardedPoints,
        String comment
) {}