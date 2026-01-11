package com.app.examproject.domains.dto.exams;

import java.time.Instant;
import java.util.UUID;

public record ExamListItemResponse(
        UUID examId,
        String title,
        String semester,
        Instant createdAt
) {}
