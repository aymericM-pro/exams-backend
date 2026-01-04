package com.app.examproject.controller.generatorexam.dtos;

public record AnswerAiResponse(
        String text,
        boolean correct,
        int position
) {}
