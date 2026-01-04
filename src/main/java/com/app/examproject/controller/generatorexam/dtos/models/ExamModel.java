package com.app.examproject.controller.generatorexam.dtos.models;

import java.util.List;

public record ExamModel(
        String title,
        String description,
        String semester,
        List<QuestionModel> questions
) {}