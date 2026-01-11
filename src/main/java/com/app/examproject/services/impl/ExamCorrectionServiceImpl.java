package com.app.examproject.services.impl;

import com.app.examproject.commons.errors.BusinessException;
import com.app.examproject.commons.errors.errors.ExamAttemptError;
import com.app.examproject.commons.errors.errors.UserError;
import com.app.examproject.domains.AnswerCorrectionMapper;
import com.app.examproject.domains.ExamCorrectionMapper;
import com.app.examproject.domains.dto.correct.CreateExamCorrectionRequest;
import com.app.examproject.domains.dto.correct.ExamCorrectionResponse;
import com.app.examproject.domains.dto.correct.QuestionCorrectionRequest;
import com.app.examproject.domains.entities.CorrectionAnswerEntity;
import com.app.examproject.domains.entities.ExamAttemptEntity;
import com.app.examproject.domains.entities.UserEntity;
import com.app.examproject.domains.entities.anwsers.StudentAnswerEntity;
import com.app.examproject.repositories.CorrectionAnswerRepository;
import com.app.examproject.repositories.ExamAttemptRepository;
import com.app.examproject.repositories.UserRepository;
import com.app.examproject.services.ExamCorrectionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class ExamCorrectionServiceImpl implements ExamCorrectionService {

    private final ExamAttemptRepository examAttemptRepository;
    private final CorrectionAnswerRepository correctionAnswerRepository;
    private final UserRepository userRepository;
    private final AnswerCorrectionMapper answerCorrectionMapper;
    private final ExamCorrectionMapper examCorrectionMapper;

    public ExamCorrectionServiceImpl(
            ExamAttemptRepository examAttemptRepository,
            CorrectionAnswerRepository correctionAnswerRepository,
            UserRepository userRepository,
            AnswerCorrectionMapper answerCorrectionMapper,
            ExamCorrectionMapper examCorrectionMapper
    ) {
        this.examAttemptRepository = examAttemptRepository;
        this.correctionAnswerRepository = correctionAnswerRepository;
        this.userRepository = userRepository;
        this.answerCorrectionMapper = answerCorrectionMapper;
        this.examCorrectionMapper = examCorrectionMapper;
    }

    @Override
    public ExamCorrectionResponse correctExam(
            UUID attemptId,
            UUID professorId,
            CreateExamCorrectionRequest request
    ) {

        ExamAttemptEntity attempt = examAttemptRepository.findByExamAttemptId(attemptId)
                .orElseThrow(() -> new BusinessException(
                        ExamAttemptError.EXAM_ATTEMPT_NOT_FOUND
                ));

        UserEntity professor = userRepository.findById(professorId)
                .orElseThrow(() -> new BusinessException(
                        UserError.USER_NOT_FOUND
                ));

        Instant correctedAt = Instant.now();

        List<CorrectionAnswerEntity> corrections =
                request.questions().stream()
                        .map(q -> correctOneQuestion(q, attempt, professor, correctedAt))
                        .toList();

        correctionAnswerRepository.saveAll(corrections);

        double finalScore = computeFinalScore(attempt, corrections);

        return examCorrectionMapper.toResponse(
                attempt,
                corrections,
                professor,
                correctedAt,
                finalScore
        );
    }

    /* ============================
       INTERNALS
       ============================ */

    private CorrectionAnswerEntity correctOneQuestion(
            QuestionCorrectionRequest request,
            ExamAttemptEntity attempt,
            UserEntity professor,
            Instant correctedAt
    ) {

        StudentAnswerEntity studentAnswer =
                attempt.getAnswers().stream()
                        .filter(a -> a.getQuestion().getQuestionId().equals(request.questionId()))
                        .findFirst()
                        .orElseThrow(() -> new BusinessException(
                                ExamAttemptError.STUDENT_ANSWER_NOT_FOUND
                        ));

        CorrectionAnswerEntity entity =
                answerCorrectionMapper.fromQuestionCorrection(request);

        entity.setStudentAnswer(studentAnswer);
        entity.setCorrectedBy(professor.getUserId());
        entity.setCorrectedAt(correctedAt);

        return entity;
    }

    private double computeFinalScore(
            ExamAttemptEntity attempt,
            List<CorrectionAnswerEntity> corrections
    ) {
        int totalQuestions = attempt.getExamSession()
                .getExam()
                .getQuestions()
                .size();

        long correctCount =
                corrections.stream()
                        .filter(CorrectionAnswerEntity::isCorrect)
                        .count();

        return totalQuestions == 0
                ? 0
                : (correctCount * 100.0) / totalQuestions;
    }

}
