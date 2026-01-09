package com.app.examproject.services;

import com.app.examproject.domains.dto.exam_attempt.ExamAttemptResponse;

import java.util.UUID;

public interface StudentExamService {

    ExamAttemptResponse start(UUID sessionId, UUID userId);
}
