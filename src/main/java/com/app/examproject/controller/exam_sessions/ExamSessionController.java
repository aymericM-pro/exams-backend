package com.app.examproject.controller.exam_sessions;

import com.app.examproject.domains.dto.exams_sessions.CreateExamSessionRequest;
import com.app.examproject.domains.dto.exams_sessions.ExamSessionResponse;
import com.app.examproject.domains.dto.exams_sessions.UpdateExamSessionRequest;
import com.app.examproject.services.ExamSessionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/exam-sessions")
public class ExamSessionController implements IExamSessionControllerSwagger {

    private final ExamSessionService examSessionService;

    public ExamSessionController(ExamSessionService examSessionService) {
        this.examSessionService = examSessionService;
    }

    @PostMapping
    @Override
    public ResponseEntity<ExamSessionResponse> create(@RequestBody CreateExamSessionRequest request) {
        ExamSessionResponse created = examSessionService.create(request);
        return ResponseEntity.status(201).body(created);
    }

    @GetMapping
    @Override
    public ResponseEntity<List<ExamSessionResponse>> getAll() {
        return ResponseEntity.ok(examSessionService.getAll());
    }

    @GetMapping("/{id}")
    @Override
    public ResponseEntity<ExamSessionResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(examSessionService.getById(id));
    }

    @PutMapping("/{id}")
    @Override
    public ResponseEntity<ExamSessionResponse> update(
            @PathVariable UUID id,
            @RequestBody UpdateExamSessionRequest request
    ) {
        return ResponseEntity.ok(examSessionService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Override
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        examSessionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
