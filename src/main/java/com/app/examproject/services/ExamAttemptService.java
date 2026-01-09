package com.app.examproject.services;

import com.app.examproject.domains.dto.exam_attempt.CreateExamAttemptRequest;
import com.app.examproject.domains.dto.exam_attempt.ExamAttemptResponse;
import com.app.examproject.domains.dto.exam_attempt.UpdateExamAttemptRequest;

import java.util.List;
import java.util.UUID;

public interface ExamAttemptService {
    ExamAttemptResponse create(CreateExamAttemptRequest request);
    List<ExamAttemptResponse> getAll();
    ExamAttemptResponse getById(UUID id);
    List<ExamAttemptResponse> getBySession(UUID examSessionId);
    ExamAttemptResponse update(UUID id, UpdateExamAttemptRequest request);
    void delete(UUID id);
}
