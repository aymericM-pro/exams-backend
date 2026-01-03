package com.app.examproject.domains.dto.exams;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ExamResponse(
        UUID examId,
        String title,
        String description,
        String semester,
        Instant createdAt,
        List<QuestionResponse> questions
) {}