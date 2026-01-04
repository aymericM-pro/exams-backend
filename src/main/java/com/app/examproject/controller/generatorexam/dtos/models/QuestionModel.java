package com.app.examproject.controller.generatorexam.dtos.models;

import java.util.List;

public record QuestionModel(
        String title,
        String type,
        int position,
        List<AnswerModel> answers
) {}
