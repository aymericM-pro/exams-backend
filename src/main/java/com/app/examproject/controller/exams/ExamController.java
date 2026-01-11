package com.app.examproject.controller.exams;

import com.app.examproject.domains.dto.exams.CreateExamRequest;
import com.app.examproject.domains.dto.exams.ExamListItemResponse;
import com.app.examproject.domains.dto.exams.ExamResponse;
import com.app.examproject.domains.dto.exams.UpdateExamRequest;
import com.app.examproject.services.ExamService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/exams")
public class ExamController implements IExamControllerSwagger {

    private final ExamService examService;

    public ExamController(ExamService examService) {
        this.examService = examService;
    }


    @PostMapping("/{userId}")
    @Override
    public ResponseEntity<ExamResponse> create(
            @PathVariable UUID userId,
            @RequestBody CreateExamRequest request
    ) {
        ExamResponse created = examService.create(userId, request);
        return ResponseEntity.status(201).body(created);
    }

    @GetMapping
    @Override
    public ResponseEntity<List<ExamListItemResponse>> getAll() {
        return ResponseEntity.ok(examService.getAll());
    }

    @GetMapping("/{id}")
    @Override
    public ResponseEntity<ExamResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(examService.getById(id));
    }

    @PutMapping("/{id}")
    @Override
    public ResponseEntity<ExamResponse> update(@PathVariable UUID id, @RequestBody UpdateExamRequest request) {
        return ResponseEntity.ok(examService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Override
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        examService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
