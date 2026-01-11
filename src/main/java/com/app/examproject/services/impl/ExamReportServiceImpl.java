package com.app.examproject.services.impl;

import com.app.examproject.domains.dto.report.ExamReportResponse;
import com.app.examproject.domains.entities.ExamAttemptEntity;
import com.app.examproject.domains.entities.QuestionEntity;
import com.app.examproject.domains.entities.anwsers.AnswerEntity;
import com.app.examproject.domains.entities.anwsers.StudentAnswerEntity;
import com.app.examproject.domains.entities.anwsers.StudentMultipleAnswerEntity;
import com.app.examproject.domains.entities.anwsers.StudentSingleAnswerEntity;
import com.app.examproject.commons.errors.BusinessException;
import com.app.examproject.commons.errors.errors.ExamAttemptError;
import com.app.examproject.repositories.ExamAttemptRepository;
import com.app.examproject.services.ExamReportMapper;
import com.app.examproject.services.ExamReportService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ExamReportServiceImpl implements ExamReportService {

    private final ExamAttemptRepository examAttemptRepository;
    private final ExamReportMapper mapper;

    public ExamReportServiceImpl(
            ExamAttemptRepository examAttemptRepository,
            ExamReportMapper mapper
    ) {
        this.examAttemptRepository = examAttemptRepository;
        this.mapper = mapper;
    }

    @Override
    public ExamReportResponse getReport(UUID attemptId) {

        ExamAttemptEntity attempt =
                examAttemptRepository.findByExamAttemptId(attemptId)
                        .orElseThrow(() ->
                                new BusinessException(
                                        ExamAttemptError.EXAM_ATTEMPT_NOT_FOUND
                                ));

        var exam = attempt.getExamSession().getExam();
        var student = attempt.getStudent();

        List<ExamReportResponse.QuestionReport> questions =
                exam.getQuestions().stream()
                        .map(q -> buildQuestionReport(q, attempt))
                        .toList();

        long correctCount = questions.stream()
                .filter(ExamReportResponse.QuestionReport::correct)
                .count();

        double score = questions.isEmpty()
                ? 0
                : (correctCount * 100.0) / questions.size();

        return new ExamReportResponse(
                mapper.toExam(exam),
                mapper.toStudent(student),
                new ExamReportResponse.Summary(
                        questions.size(),
                        (int) correctCount,
                        score,
                        attempt.getSubmittedAt()
                ),
                questions
        );
    }

    private ExamReportResponse.QuestionReport buildQuestionReport(
            QuestionEntity question,
            ExamAttemptEntity attempt
    ) {

        StudentAnswerEntity studentAnswer = attempt.getAnswers().stream()
                        .filter(a -> a.getQuestion().equals(question))
                        .findFirst()
                        .orElse(null);

        boolean isCorrect = studentAnswer != null && studentAnswer.isCorrect();

        Set<UUID> selectedIds = extractSelectedAnswerIds(studentAnswer);

        List<ExamReportResponse.AnswerReport> answers =
                question.getAnswers().stream()
                        .map(a ->
                                mapper.toAnswerReport(
                                        a,
                                        selectedIds.contains(a.getAnswerId())
                                )
                        )
                        .toList();

        return mapper.toQuestionReport(
                question,
                isCorrect,
                answers
        );
    }

    private Set<UUID> extractSelectedAnswerIds(StudentAnswerEntity studentAnswer) {
        if (studentAnswer == null) {
            return Set.of();
        }
        if (studentAnswer instanceof StudentSingleAnswerEntity singleAnswer) {
            return Set.of(singleAnswer.getSelectedAnswer().getAnswerId());
        } else if (studentAnswer instanceof StudentMultipleAnswerEntity multipleAnswer) {
            return multipleAnswer.getSelectedAnswers().stream()
                    .map(AnswerEntity::getAnswerId)
                    .collect(Collectors.toSet());
        }
        return Set.of();
    }
}
