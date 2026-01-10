package com.app.examproject.controller.student_exam;

import java.util.List;

public record SubmitExamAnswersRequest(
        List<SubmitQuestionAnswerRequest> answers
) {}