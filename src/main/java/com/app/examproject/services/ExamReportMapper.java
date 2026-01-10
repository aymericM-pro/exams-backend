package com.app.examproject.services;

import com.app.examproject.domains.dto.report.ExamReportResponse;
import com.app.examproject.domains.entities.ExamEntity;
import com.app.examproject.domains.entities.QuestionEntity;
import com.app.examproject.domains.entities.UserEntity;
import com.app.examproject.domains.entities.anwsers.AnswerEntity;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ExamReportMapper {

    @Mapping(target = "examId", source = "exam.examId")
    ExamReportResponse.Exam toExam(ExamEntity exam);

    @Mapping(target = "userId", source = "user.userId")
    ExamReportResponse.Student toStudent(UserEntity user);

    @Mapping(target = "questionId", source = "question.questionId")
    @Mapping(target = "title", source = "question.title")
    @Mapping(target = "type", source = "question.type")
    @Mapping(target = "correct", source = "isCorrect")
    @Mapping(target = "answers", source = "answers")
    ExamReportResponse.QuestionReport toQuestionReport(
            QuestionEntity question,
            boolean isCorrect,
            List<ExamReportResponse.AnswerReport> answers
    );

    @Mapping(target = "answerId", source = "answer.answerId")
    @Mapping(target = "text", source = "answer.text")
    @Mapping(target = "correct", source = "answer.correct")
    @Mapping(target = "selectedByStudent", source = "selected")
    ExamReportResponse.AnswerReport toAnswerReport(
            AnswerEntity answer,
            boolean selected
    );
}

