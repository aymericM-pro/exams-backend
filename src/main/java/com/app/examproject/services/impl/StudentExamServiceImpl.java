package com.app.examproject.services.impl;

import com.app.examproject.domains.ExamAttemptMapper;
import com.app.examproject.domains.dto.exam_attempt.ExamAttemptResponse;
import com.app.examproject.domains.entities.ExamAttemptEntity;
import com.app.examproject.domains.entities.UserEntity;
import com.app.examproject.domains.entities.sessions.ExamSessionEntity;
import com.app.examproject.errors.BusinessException;
import com.app.examproject.errors.errors.ExamAttemptError;
import com.app.examproject.errors.errors.ExamSessionError;
import com.app.examproject.errors.errors.UserError;
import com.app.examproject.repositories.ExamAttemptRepository;
import com.app.examproject.repositories.ExamSessionRepository;
import com.app.examproject.repositories.UserRepository;
import com.app.examproject.services.StudentExamService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@Transactional
public class StudentExamServiceImpl implements StudentExamService {

    private final ExamSessionRepository examSessionRepository;
    private final ExamAttemptRepository examAttemptRepository;
    private final UserRepository userRepository;
    private final ExamAttemptMapper examAttemptMapper;

    public StudentExamServiceImpl(
            ExamSessionRepository examSessionRepository,
            ExamAttemptRepository examAttemptRepository,
            UserRepository userRepository,
            ExamAttemptMapper examAttemptMapper
    ) {
        this.examSessionRepository = examSessionRepository;
        this.examAttemptRepository = examAttemptRepository;
        this.userRepository = userRepository;
        this.examAttemptMapper = examAttemptMapper;
    }

    public ExamAttemptResponse start(UUID sessionId, UUID userId) {

        ExamSessionEntity session = examSessionRepository.findById(sessionId)
                .orElseThrow(() -> new BusinessException(ExamSessionError.EXAM_SESSION_NOT_FOUND));

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(UserError.USER_NOT_FOUND));

        // Vérifier que l'étudiant est bien dans la classe de la session
        if (user.getStudentClass() == null || !user.getStudentClass().getClassId().equals(session.getStudentClass().getClassId())) {
            throw new BusinessException(ExamAttemptError.USER_NOT_ALLOWED_FOR_SESSION);
        }

        // Si déjà une tentative -> on la renvoie
        return examAttemptRepository
                .findByExamSession_ExamSessionIdAndStudent_UserId(sessionId, userId)
                .map(examAttemptMapper::toResponse)
                .orElseGet(() -> {
                    ExamAttemptEntity attempt = new ExamAttemptEntity();
                    attempt.setExamSession(session);
                    attempt.setStudent(user);
                    attempt.setStartedAt(Instant.now());
                    attempt.setSubmittedAt(null);

                    ExamAttemptEntity saved = examAttemptRepository.save(attempt);
                    return examAttemptMapper.toResponse(saved);
                });
    }

}
