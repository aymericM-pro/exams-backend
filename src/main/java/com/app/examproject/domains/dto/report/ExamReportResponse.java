package com.app.examproject.domains.dto.report;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ExamReportResponse(

        Exam exam,
        Student student,
        Summary summary,
        List<QuestionReport> questions

) {

    // ================= EXAM =================
    public record Exam(
            UUID examId,
            String title,
            String description,
            String semester,
            Instant createdAt
    ) {}

    // ================= STUDENT =================
    public record Student(
            UUID userId,
            String firstname,
            String lastname
    ) {}

    // ================= SUMMARY =================
    public record Summary(
            int totalQuestions,
            int correctAnswers,
            double score,
            Instant submittedAt
    ) {}

    // ================= QUESTIONS =================
    public record QuestionReport(
            UUID questionId,
            String title,
            String type,
            boolean correct,
            List<AnswerReport> answers
    ) {}

    // ================= ANSWERS =================
    public record AnswerReport(
            UUID answerId,
            String text,
            boolean correct,
            boolean selectedByStudent
    ) {}
}
