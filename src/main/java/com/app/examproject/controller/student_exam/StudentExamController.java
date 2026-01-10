package com.app.examproject.controller.student_exam;

import com.app.examproject.domains.dto.exam_attempt.ExamAttemptResponse;
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

    public StudentExamController(StudentExamService studentExamService,
                                 StudentAnswerService studentAnswerService) {
        this.studentExamService = studentExamService;
        this.studentAnswerService = studentAnswerService;
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
    
    @PostMapping("/{attemptId}/answers")
    public ResponseEntity<Void> submitExamAnswers(
            @PathVariable UUID attemptId,
            @RequestBody SubmitExamAnswersRequest request
    ) {
        studentAnswerService.submitAnswers(attemptId, request);
        return ResponseEntity.noContent().build();
    }
}
