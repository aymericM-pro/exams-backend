package com.app.examproject.services;

import com.app.examproject.domains.dto.exams.CreateExamRequest;
import com.app.examproject.domains.dto.exams.ExamResponse;
import com.app.examproject.domains.dto.exams.UpdateExamRequest;
import java.util.List;

import java.util.UUID;


public interface ExamService {
    ExamResponse create(UUID userId, CreateExamRequest request);
    List<ExamResponse> getAll();
    ExamResponse getById(UUID id);
    ExamResponse update(UUID id, UpdateExamRequest request);
    void delete(UUID id);
}