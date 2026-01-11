package com.app.examproject.domains.dto.correct;

import java.time.Instant;
import java.util.UUID;

public record CorrectionAnswerResponse(
        UUID correctionId,
        UUID studentAnswerId,
        Boolean correct,
        Double score,
        String comment,
        UUID correctedBy,
        Instant correctedAt) {
}