package com.app.examproject.services;

import com.app.examproject.domains.dto.exams_sessions.CreateExamSessionRequest;
import com.app.examproject.domains.dto.exams_sessions.ExamSessionResponse;
import com.app.examproject.domains.dto.exams_sessions.UpdateExamSessionRequest;

import java.util.List;
import java.util.UUID;

public interface ExamSessionService {
    ExamSessionResponse create(CreateExamSessionRequest request);
    List<ExamSessionResponse> getAll();
    ExamSessionResponse getById(UUID id);
    ExamSessionResponse update(UUID id, UpdateExamSessionRequest request);
    void delete(UUID id);
}
