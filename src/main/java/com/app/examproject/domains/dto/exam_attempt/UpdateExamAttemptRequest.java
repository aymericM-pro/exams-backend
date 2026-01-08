package com.app.examproject.domains.dto.exam_attempt;

import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record UpdateExamAttemptRequest(

        @NotNull
        Instant submittedAt
) {
}