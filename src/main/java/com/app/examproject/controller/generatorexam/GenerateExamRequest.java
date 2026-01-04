package com.app.examproject.controller.generatorexam;

public record GenerateExamRequest(
        String theme,
        int questionCount,
        String level // "beginner", "intermediate", "advanced"
) {}
