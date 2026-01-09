package com.app.examproject.controller.exam_attempts;

import com.app.examproject.domains.dto.exam_attempt.CreateExamAttemptRequest;
import com.app.examproject.domains.dto.exam_attempt.ExamAttemptResponse;
import com.app.examproject.domains.dto.exam_attempt.UpdateExamAttemptRequest;
import com.app.examproject.services.ExamAttemptService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/exam-attempts")
public class ExamAttemptController implements IExamAttemptControllerSwagger {

    private final ExamAttemptService examAttemptService;

    public ExamAttemptController(ExamAttemptService examAttemptService) {
        this.examAttemptService = examAttemptService;
    }

    @GetMapping
    @PostMapping
    public ResponseEntity<ExamAttemptResponse> create(
            @RequestBody CreateExamAttemptRequest request
    ) {
        return ResponseEntity.ok(examAttemptService.create(request));
    }

    @GetMapping("/{id}")
    @Override
    public ResponseEntity<ExamAttemptResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(examAttemptService.getById(id));
    }

    @GetMapping("/session/{examSessionId}")
    @Override
    public ResponseEntity<List<ExamAttemptResponse>> getBySession(
            @PathVariable UUID examSessionId
    ) {
        return ResponseEntity.ok(examAttemptService.getBySession(examSessionId));
    }

    @PutMapping("/{id}")
    @Override
    public ResponseEntity<ExamAttemptResponse> update(
            @PathVariable UUID id,
            @RequestBody UpdateExamAttemptRequest request
    ) {
        return ResponseEntity.ok(examAttemptService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Override
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        examAttemptService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
