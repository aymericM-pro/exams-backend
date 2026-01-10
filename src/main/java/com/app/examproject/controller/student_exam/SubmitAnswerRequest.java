package com.app.examproject.controller.student_exam;

import com.app.examproject.domains.AnswerType;

import java.util.List;
import java.util.UUID;

public record SubmitAnswerRequest(
        UUID questionId,
        AnswerType type,
        UUID answerId,
        List<UUID> answerIds,
        String content
) {}
