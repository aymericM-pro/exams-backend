package com.app.examproject.services.impl;

import com.app.examproject.controller.student_exam.SubmitExamAnswersRequest;
import com.app.examproject.controller.student_exam.SubmitQuestionAnswerRequest;
import com.app.examproject.domains.entities.ExamAttemptEntity;
import com.app.examproject.domains.entities.QuestionEntity;
import com.app.examproject.domains.entities.anwsers.*;
import com.app.examproject.errors.BusinessException;
import com.app.examproject.errors.errors.AnswerError;
import com.app.examproject.errors.errors.ExamAttemptError;
import com.app.examproject.errors.errors.QuestionError;
import com.app.examproject.repositories.AnswerRepository;
import com.app.examproject.repositories.ExamAttemptRepository;
import com.app.examproject.repositories.QuestionRepository;
import com.app.examproject.repositories.StudentAnswerRepository;
import com.app.examproject.services.StudentAnswerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class StudentAnswerServiceImpl implements StudentAnswerService {

    private final ExamAttemptRepository examAttemptRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final StudentAnswerRepository studentAnswerRepository;

    @Override
    @Transactional
    public void submitAnswers(UUID attemptId, SubmitExamAnswersRequest request) {

        ExamAttemptEntity attempt = examAttemptRepository.findById(attemptId)
                .orElseThrow(() -> new BusinessException(ExamAttemptError.EXAM_ATTEMPT_NOT_FOUND));

        if (attempt.getSubmittedAt() != null) {
            throw new BusinessException(ExamAttemptError.EXAM_ATTEMPT_ALREADY_SUBMITTED);
        }

        // 🔥 SUPPRESSION RÉELLE EN BASE
        studentAnswerRepository.deleteByExamAttempt(attempt);
        attempt.getAnswers().clear();

        for (SubmitQuestionAnswerRequest a : request.answers()) {

            QuestionEntity question = questionRepository.findById(a.questionId())
                    .orElseThrow(() -> new BusinessException(QuestionError.QUESTION_NOT_FOUND));

            StudentAnswerEntity answer = buildAnswer(a);

            answer.setExamAttempt(attempt);
            answer.setQuestion(question);
            answer.validate();

            attempt.getAnswers().add(answer);
        }

        examAttemptRepository.save(attempt);
    }


    private StudentAnswerEntity buildAnswer(SubmitQuestionAnswerRequest request) {

        QuestionEntity question = questionRepository.findById(request.questionId())
                .orElseThrow(() -> new BusinessException(QuestionError.QUESTION_NOT_FOUND));

        return switch (question.getType()) {

            case SINGLE -> {
                if (request.answerId() == null) {
                    throw new BusinessException(AnswerError.SINGLE_ANSWER_REQUIRED);
                }

                AnswerEntity selected = answerRepository.findById(request.answerId())
                        .orElseThrow(() -> new BusinessException(AnswerError.ANSWER_NOT_FOUND));

                StudentSingleAnswerEntity answer = new StudentSingleAnswerEntity();
                answer.setSelectedAnswer(selected);
                yield answer;
            }

            case MULTIPLE -> {
                if (request.answerIds() == null || request.answerIds().isEmpty()) {
                    throw new BusinessException(AnswerError.MULTIPLE_ANSWERS_REQUIRED);
                }

                StudentMultipleAnswerEntity answer = new StudentMultipleAnswerEntity();
                answer.setSelectedAnswers(
                        request.answerIds().stream()
                                .map(id -> answerRepository.findById(id)
                                        .orElseThrow(() -> new BusinessException(AnswerError.ANSWER_NOT_FOUND)))
                                .collect(Collectors.toSet())
                );
                yield answer;
            }

            case TEXT -> {
                if (request.content() == null || request.content().isBlank()) {
                    throw new BusinessException(AnswerError.TEXT_ANSWER_REQUIRED);
                }

                StudentTextAnswerEntity answer = new StudentTextAnswerEntity();
                answer.setContent(request.content());
                yield answer;
            }
        };
    }

}
