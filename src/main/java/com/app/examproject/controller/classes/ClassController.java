package com.app.examproject.controller.classes;

import com.app.examproject.domains.dto.classes.ClassResponse;
import com.app.examproject.domains.dto.classes.CreateClassRequest;
import com.app.examproject.domains.dto.users.StudentResponse;
import com.app.examproject.services.ClassService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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

    @GetMapping("/me")
    public ResponseEntity<List<ClassResponse>> getMyClasses() {
        UUID teacherId = UUID.fromString("78c1263b-f1f4-42dd-9298-bc9852a23853");
        return ResponseEntity.ok(classService.getClassesByTeacher(teacherId));
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
    public ResponseEntity<List<StudentResponse>> getStudentsByClass(
            @PathVariable UUID classId
    ) {
        return ResponseEntity.ok(classService.getStudentsByClass(classId));
    }

    @GetMapping("/{classId}/students/pdf")
    public ResponseEntity<byte[]> exportStudentsPdf(@PathVariable UUID classId) {

        byte[] pdfBytes = classService.exportStudentsPdf(classId);

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=class-students.pdf"
                )
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }

    @DeleteMapping("/{classId}/students/{studentId}")
    @Override
    public ResponseEntity<Void> removeStudentFromClass(
            @PathVariable UUID classId,
            @PathVariable UUID studentId
    ) {
        classService.removeStudentFromClass(classId, studentId);
        return ResponseEntity.noContent().build();
    }
}
