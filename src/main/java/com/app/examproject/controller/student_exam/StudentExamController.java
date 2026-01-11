package com.app.examproject.controller.student_exam;

import com.app.examproject.domains.dto.correct.CreateExamCorrectionRequest;
import com.app.examproject.domains.dto.correct.ExamCorrectionResponse;
import com.app.examproject.domains.dto.exam_attempt.ExamAttemptResponse;
import com.app.examproject.domains.dto.report.ExamReportResponse;
import com.app.examproject.services.ExamCorrectionService;
import com.app.examproject.services.ExamReportService;
import com.app.examproject.services.StudentAnswerService;
import com.app.examproject.services.StudentExamService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/exam-sessions")
public class StudentExamController {

    private final StudentExamService studentExamService;
    private final StudentAnswerService studentAnswerService;
    private final ExamReportService examReportService;
    private final ExamCorrectionService examCorrectionService;

    public StudentExamController(
            StudentExamService studentExamService,
            StudentAnswerService studentAnswerService,
            ExamReportService examReportService,
            ExamCorrectionService examCorrectionService
    ) {
        this.studentExamService = studentExamService;
        this.studentAnswerService = studentAnswerService;
        this.examReportService = examReportService;
        this.examCorrectionService = examCorrectionService;
    }

    @PostMapping("/{sessionId}/start")
    public ResponseEntity<ExamAttemptResponse> start(
            @PathVariable UUID sessionId,
            @RequestParam UUID userId
    ) {
        return ResponseEntity.status(201).body(
                studentExamService.start(sessionId, userId)
        );
    }

    @PostMapping("/attempts/{attemptId}/answers")
    public ResponseEntity<Void> submitExamAnswers(
            @PathVariable UUID attemptId,
            @RequestBody SubmitExamAnswersRequest request
    ) {
        studentAnswerService.submitAnswers(attemptId, request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/attempts/{attemptId}/report")
    public ResponseEntity<ExamReportResponse> getReport(
            @PathVariable UUID attemptId
    ) {
        return ResponseEntity.ok(
                examReportService.getReport(attemptId)
        );
    }

    @PostMapping("/attempts/{attemptId}/correct")
    public ResponseEntity<ExamCorrectionResponse> correctExam(
            @PathVariable UUID attemptId,
            @RequestParam UUID professorId,
            @RequestBody CreateExamCorrectionRequest request
    ) {
        return ResponseEntity.ok(
                examCorrectionService.correctExam(
                        attemptId,
                        professorId,
                        request
                )
        );
    }
}
