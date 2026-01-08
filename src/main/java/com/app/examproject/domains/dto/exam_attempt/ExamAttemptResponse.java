package com.app.examproject.domains.dto.exam_attempt;

import java.time.Instant;
import java.util.UUID;

public record ExamAttemptResponse(
    UUID examAttemptId,
    UUID examSessionId,
    UUID studentId,
    Instant startedAt,
    Instant submittedAt) {
}
