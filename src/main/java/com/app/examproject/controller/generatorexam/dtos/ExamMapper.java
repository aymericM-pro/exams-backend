package com.app.examproject.controller.generatorexam.dtos;

import com.app.examproject.controller.generatorexam.dtos.models.ExamModel;
import com.app.examproject.domains.dto.exams.AnswerResponse;
import com.app.examproject.domains.dto.exams.ExamResponse;
import com.app.examproject.domains.dto.exams.QuestionResponse;

import java.time.Instant;
import java.util.UUID;

public final class ExamMapper {

    private ExamMapper() {}

    public static ExamResponse toResponse(ExamModel model) {
        return new ExamResponse(
                UUID.randomUUID(),
                model.title(),
                model.description(),
                model.semester(),
                Instant.now(),
                model.questions().stream()
                        .map(q -> new QuestionResponse(
                                UUID.randomUUID(),
                                q.title(),
                                q.type(),
                                q.position(),
                                q.answers().stream()
                                        .map(a -> new AnswerResponse(
                                                UUID.randomUUID(),
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
