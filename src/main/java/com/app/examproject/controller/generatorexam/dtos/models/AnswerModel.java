package com.app.examproject.controller.generatorexam.dtos.models;

public record AnswerModel(
        String text,
        boolean correct,
        int position
) {}
