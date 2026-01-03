package com.app.examproject.domains.dto.exams;

import java.util.UUID;

public record UpdateAnswerRequest(
        UUID id,
        String text,
        boolean correct
) {}
