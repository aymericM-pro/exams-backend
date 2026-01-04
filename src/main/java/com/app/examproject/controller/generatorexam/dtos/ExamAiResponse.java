package com.app.examproject.controller.generatorexam.dtos;

import java.time.Instant;
import java.util.List;

public record ExamAiResponse(
        String title,
        String description,
        String semester,
        Instant createdAt,
        List<QuestionAiResponse> questions
) {}