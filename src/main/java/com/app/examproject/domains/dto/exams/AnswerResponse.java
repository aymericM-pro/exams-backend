package com.app.examproject.domains.dto.exams;

import java.util.UUID;

public record AnswerResponse(
        UUID answerId,
        String text,
        boolean correct,
        int position
) {}