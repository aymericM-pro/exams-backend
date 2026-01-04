package com.app.examproject.controller.generatorexam.dtos;

import java.util.List;

public record QuestionAiResponse(
        String title,
        String type,
        int position,
        List<AnswerAiResponse> answers
) {}