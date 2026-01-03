package com.app.examproject.domains.dto.exams;

import java.util.List;
import java.util.UUID;

public record QuestionResponse(
        UUID questionId,
        String title,
        String type,
        int position,
        List<AnswerResponse> answers
) {}
