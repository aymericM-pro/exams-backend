package com.app.examproject.domains.dto.exams;

public record CreateAnswerRequest(
        String text,
        boolean correct
) {}
