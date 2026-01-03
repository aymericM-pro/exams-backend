package com.app.examproject.services.impl;

import com.app.examproject.domains.ExamMapper;
import com.app.examproject.domains.dto.exams.CreateExamRequest;
import com.app.examproject.domains.dto.exams.ExamResponse;
import com.app.examproject.domains.dto.exams.UpdateExamRequest;
import com.app.examproject.domains.entities.ExamEntity;
import com.app.examproject.domains.entities.QuestionEntity;
import com.app.examproject.domains.entities.AnswerEntity;
import com.app.examproject.domains.entities.UserEntity;
import com.app.examproject.errors.BusinessException;
import com.app.examproject.errors.ExamError;
import com.app.examproject.errors.UserError;
import com.app.examproject.repositories.ExamRepository;
import com.app.examproject.repositories.UserRepository;
import com.app.examproject.services.ExamService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class ExamServiceImpl implements ExamService {

    private final ExamRepository examRepository;
    private final ExamMapper examMapper;
    private final UserRepository userRepository;
    public ExamServiceImpl(
            ExamRepository examRepository,
            ExamMapper examMapper,
            UserRepository userRepository
    ) {
        this.examRepository = examRepository;
        this.examMapper = examMapper;
        this.userRepository = userRepository;
    }

    // ================= CREATE =================
    @Override
    public ExamResponse create(UUID userId, CreateExamRequest request) {
        validateCreate(request);

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(UserError.USER_NOT_FOUND));

        ExamEntity exam = examMapper.fromCreate(request);
        exam.setUser(user);

        linkRelations(exam);

        ExamEntity saved = examRepository.save(exam);
        return examMapper.toResponse(saved);
    }
    // ================= READ =================

    @Override
    @Transactional(readOnly = true)
    public List<ExamResponse> getAll() {
        return examRepository.findAll()
                .stream()
                .map(examMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ExamResponse getById(UUID id) {
        ExamEntity exam = examRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ExamError.EXAM_NOT_FOUND));

        return examMapper.toResponse(exam);
    }

    // ================= UPDATE =================

    @Override
    public ExamResponse update(UUID id, UpdateExamRequest request) {
        validateUpdate(request);

        ExamEntity existing = examRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ExamError.EXAM_NOT_FOUND));

        // clear children (orphanRemoval = true)
        existing.clearQuestions();

        ExamEntity updated = examMapper.fromUpdate(request);

        existing.setTitle(updated.getTitle());
        existing.setDescription(updated.getDescription());
        existing.setSemester(updated.getSemester());
        existing.setQuestions(updated.getQuestions());

        linkRelations(existing);

        ExamEntity saved = examRepository.save(existing);
        return examMapper.toResponse(saved);
    }

    // ================= DELETE =================

    @Override
    public void delete(UUID id) {
        if (!examRepository.existsById(id)) {
            throw new BusinessException(ExamError.EXAM_NOT_FOUND);
        }
        examRepository.deleteById(id);
    }

    // ================= VALIDATION =================

    private void validateCreate(CreateExamRequest request) {
        if (request.title() == null || request.title().isBlank()) {
            throw new BusinessException(ExamError.INVALID_REQUEST);
        }
        if (request.semester() == null || request.semester().isBlank()) {
            throw new BusinessException(ExamError.INVALID_REQUEST);
        }
        if (request.questions() == null || request.questions().isEmpty()) {
            throw new BusinessException(ExamError.INVALID_REQUEST);
        }
    }

    private void validateUpdate(UpdateExamRequest request) {
        if (request.title() == null || request.title().isBlank()) {
            throw new BusinessException(ExamError.INVALID_REQUEST);
        }
        if (request.semester() == null || request.semester().isBlank()) {
            throw new BusinessException(ExamError.INVALID_REQUEST);
        }
    }

    // ================= RELATION LINKING =================
    private void linkRelations(ExamEntity exam) {
        if (exam.getQuestions() == null) return;

        for (QuestionEntity question : exam.getQuestions()) {
            question.setExam(exam);

            if (question.getAnswers() != null) {
                for (AnswerEntity answer : question.getAnswers()) {
                    answer.setQuestion(question);
                }
            }
        }
    }
}
