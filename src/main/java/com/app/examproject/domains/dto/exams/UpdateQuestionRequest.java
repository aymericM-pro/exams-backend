package com.app.examproject.domains.dto.exams;

import java.util.List;
import java.util.UUID;

public record UpdateQuestionRequest(
        UUID id,              // optionnel : si null -> on recrée
        String title,
        String type,
        List<UpdateAnswerRequest> answers
) {}