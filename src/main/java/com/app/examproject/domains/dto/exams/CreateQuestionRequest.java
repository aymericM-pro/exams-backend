package com.app.examproject.domains.dto.exams;

import java.util.List;

public record CreateQuestionRequest(
        String title,
        String type,          // "single" | "multiple" | "text"
        List<CreateAnswerRequest> answers
) {}
