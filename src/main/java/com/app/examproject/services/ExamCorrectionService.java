package com.app.examproject.services;

import com.app.examproject.domains.dto.correct.CreateExamCorrectionRequest;
import com.app.examproject.domains.dto.correct.ExamCorrectionResponse;

import java.util.UUID;

public interface ExamCorrectionService {

    ExamCorrectionResponse correctExam(
            UUID attemptId,
            UUID professorId,
            CreateExamCorrectionRequest request
    );
}