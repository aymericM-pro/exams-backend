package com.app.examproject.controller.classes;

import com.app.examproject.domains.dto.classes.ClassResponse;
import com.app.examproject.domains.dto.classes.CreateClassRequest;
import com.app.examproject.domains.dto.users.StudentResponse;
import com.app.examproject.services.ClassService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/classes")
public class ClassController implements IClassControllerSwagger {

    private final ClassService classService;

    public ClassController(ClassService classService) {
        this.classService = classService;
    }

    @PostMapping
    @Override
    public ResponseEntity<ClassResponse> create(@RequestBody CreateClassRequest request) {
        ClassResponse created = classService.create(request);
        return ResponseEntity.status(201).body(created);
    }

    @GetMapping
    @Override
    public ResponseEntity<List<ClassResponse>> getAll() {
        return ResponseEntity.ok(classService.getAll());
    }

    @GetMapping("/{id}")
    @Override
    public ResponseEntity<ClassResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(classService.getById(id));
    }

    @DeleteMapping("/{id}")
    @Override
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        classService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{classId}/students")
    @Override
    public ResponseEntity<List<StudentResponse>> getStudentsByClass(@PathVariable UUID classId) {
        return ResponseEntity.ok(classService.getStudentsByClass(classId));
    }

}
