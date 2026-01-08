package com.app.examproject.services.impl;

import com.app.examproject.domains.ExamAttemptMapper;
import com.app.examproject.domains.dto.exam_attempt.ExamAttemptResponse;
import com.app.examproject.domains.dto.exam_attempt.UpdateExamAttemptRequest;
import com.app.examproject.domains.entities.ExamAttemptEntity;
import com.app.examproject.errors.BusinessException;
import com.app.examproject.errors.errors.ExamAttemptError;
import com.app.examproject.repositories.ExamAttemptRepository;
import com.app.examproject.services.ExamAttemptService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class ExamAttemptServiceImpl implements ExamAttemptService {

    private final ExamAttemptRepository examAttemptRepository;
    private final ExamAttemptMapper examAttemptMapper;

    public ExamAttemptServiceImpl(
            ExamAttemptRepository examAttemptRepository,
            ExamAttemptMapper examAttemptMapper
    ) {
        this.examAttemptRepository = examAttemptRepository;
        this.examAttemptMapper = examAttemptMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public ExamAttemptResponse getById(UUID id) {
        ExamAttemptEntity attempt = examAttemptRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ExamAttemptError.EXAM_ATTEMPT_NOT_FOUND));

        return examAttemptMapper.toResponse(attempt);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExamAttemptResponse> getBySession(UUID examSessionId) {
        return examAttemptRepository.findByExamSession_ExamSessionId(examSessionId)
                .stream()
                .map(examAttemptMapper::toResponse)
                .toList();
    }

    @Override
    public ExamAttemptResponse update(UUID id, UpdateExamAttemptRequest request) {
        ExamAttemptEntity existing = examAttemptRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ExamAttemptError.EXAM_ATTEMPT_NOT_FOUND));

        examAttemptMapper.update(request, existing);

        ExamAttemptEntity saved = examAttemptRepository.save(existing);
        return examAttemptMapper.toResponse(saved);
    }

    @Override
    public void delete(UUID id) {
        if (!examAttemptRepository.existsById(id)) {
            throw new BusinessException(ExamAttemptError.EXAM_ATTEMPT_NOT_FOUND);
        }
        examAttemptRepository.deleteById(id);
    }
}
