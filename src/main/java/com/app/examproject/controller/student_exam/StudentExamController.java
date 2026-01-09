package com.app.examproject.controller.student_exam;

import com.app.examproject.domains.dto.exam_attempt.ExamAttemptResponse;
import com.app.examproject.services.StudentExamService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/exam-sessions")
public class StudentExamController {

    private final StudentExamService studentExamService;

    public StudentExamController(StudentExamService studentExamService) {
        this.studentExamService = studentExamService;
    }

    @PostMapping("/{sessionId}/start")
    public ResponseEntity<ExamAttemptResponse> start(
            @PathVariable UUID sessionId,
            @RequestParam UUID userId
    ) {
        return ResponseEntity.status(201).body(studentExamService.start(sessionId, userId));
    }
}
