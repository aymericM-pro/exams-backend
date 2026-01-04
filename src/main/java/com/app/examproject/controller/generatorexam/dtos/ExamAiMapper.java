package com.app.examproject.controller.generatorexam.dtos;

import com.app.examproject.controller.generatorexam.dtos.models.AnswerModel;
import com.app.examproject.controller.generatorexam.dtos.models.ExamModel;
import com.app.examproject.controller.generatorexam.dtos.models.QuestionModel;

public final class ExamAiMapper {

    private ExamAiMapper() {}

    public static ExamModel toModel(ExamAiResponse ai) {
        return new ExamModel(
                ai.title(),
                ai.description(),
                ai.semester(),
                ai.questions().stream()
                        .map(q -> new QuestionModel(
                                q.title(),
                                q.type(),
                                q.position(),
                                q.answers().stream()
                                        .map(a -> new AnswerModel(
                                                a.text(),
                                                a.correct(),
                                                a.position()
                                        ))
                                        .toList()
                        ))
                        .toList()
        );
    }
}
