package com.app.examproject.services;

import com.app.examproject.controller.student_exam.SubmitExamAnswersRequest;

import java.util.UUID;

public interface StudentAnswerService {
    void submitAnswers(UUID attemptId, SubmitExamAnswersRequest request);
}
