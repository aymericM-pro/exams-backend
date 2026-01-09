package com.app.examproject.domains.dto.exam_attempt;

import java.util.UUID;

public record CreateExamAttemptRequest(
        UUID examSessionId,
        UUID studentId
) {}
