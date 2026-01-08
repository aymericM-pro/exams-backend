package com.app.examproject.domains.dto.exam_attempt;

import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.UUID;

public record CreateExamAttemptRequest(

        @NotNull
        UUID examSessionId,

        @NotNull
        UUID studentId,

        @NotNull
        Instant startedAt

) {
}