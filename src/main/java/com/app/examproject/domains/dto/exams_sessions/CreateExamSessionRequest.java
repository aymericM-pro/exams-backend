package com.app.examproject.domains.dto.exams_sessions;

import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.UUID;

public record CreateExamSessionRequest(
        @NotNull
        UUID examId,
        @NotNull
        UUID classId,
        @NotNull
        Instant startAt,
        @NotNull
        Instant endAt) {
}
