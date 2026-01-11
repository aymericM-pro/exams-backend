package com.app.examproject.services.impl;


import com.app.examproject.domains.ExamSessionMapper;
import com.app.examproject.domains.dto.exams_sessions.CreateExamSessionRequest;
import com.app.examproject.domains.dto.exams_sessions.ExamSessionResponse;
import com.app.examproject.domains.dto.exams_sessions.UpdateExamSessionRequest;
import com.app.examproject.domains.entities.ClassEntity;
import com.app.examproject.domains.entities.ExamEntity;
import com.app.examproject.domains.entities.sessions.ExamSessionEntity;
import com.app.examproject.commons.errors.BusinessException;
import com.app.examproject.commons.errors.errors.ExamSessionError;
import com.app.examproject.repositories.ClassRepository;
import com.app.examproject.repositories.ExamRepository;
import com.app.examproject.repositories.ExamSessionRepository;
import com.app.examproject.services.ExamSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class ExamSessionServiceImpl implements ExamSessionService {

    private final ExamSessionRepository examSessionRepository;
    private final ExamRepository examRepository;
    private final ClassRepository classRepository;
    private final ExamSessionMapper examSessionMapper;

    @Override
    public ExamSessionResponse create(CreateExamSessionRequest request) {

        ExamEntity exam = examRepository.findById(request.examId())
                .orElseThrow(() -> new BusinessException(ExamSessionError.EXAM_NOT_FOUND));

        ClassEntity studentClass = classRepository.findById(request.classId())
                .orElseThrow(() -> new BusinessException(ExamSessionError.CLASS_NOT_FOUND));

        ExamSessionEntity session =
                examSessionMapper.toEntity(request, exam, studentClass);

        ExamSessionEntity saved = examSessionRepository.save(session);

        return examSessionMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExamSessionResponse> getAll() {
        return examSessionRepository.findAll()
                .stream()
                .map(examSessionMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ExamSessionResponse getById(UUID id) {
        ExamSessionEntity session = examSessionRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ExamSessionError.EXAM_SESSION_NOT_FOUND));

        return examSessionMapper.toResponse(session);
    }

    @Override
    public ExamSessionResponse update(UUID id, UpdateExamSessionRequest request) {

        ExamSessionEntity existing = examSessionRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ExamSessionError.EXAM_SESSION_NOT_FOUND));

        examSessionMapper.updateEntity(existing, request);

        ExamSessionEntity saved = examSessionRepository.save(existing);

        return examSessionMapper.toResponse(saved);
    }

    @Override
    public void delete(UUID id) {
        if (!examSessionRepository.existsById(id)) {
            throw new BusinessException(ExamSessionError.EXAM_SESSION_NOT_FOUND);
        }
        examSessionRepository.deleteById(id);
    }
}
