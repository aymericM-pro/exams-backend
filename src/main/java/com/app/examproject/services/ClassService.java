package com.app.examproject.services;

import com.app.examproject.domains.dto.classes.ClassResponse;
import com.app.examproject.domains.dto.classes.CreateClassRequest;
import com.app.examproject.domains.dto.users.StudentResponse;

import java.util.List;
import java.util.UUID;

public interface ClassService {
    ClassResponse create(CreateClassRequest request);
    List<ClassResponse> getAll();
    List<StudentResponse> getStudentsByClass(UUID classId);
    ClassResponse getById(UUID id);
    void delete(UUID id);
    byte[] exportStudentsPdf(UUID classId);
}
