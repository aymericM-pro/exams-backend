package com.app.examproject.domains.dto.classes.pdf;

import java.util.List;

public record ClassStudentsPdfRequest(
        String className,
        String promotion,
        String academicYear,
        String graduationYear,
        List<ProfessorRow> professors,
        List<StudentRow> students
) {
    public record ProfessorRow(
            String subject,
            String name,
            String email
    ) {}

    public record StudentRow(
            String firstname,
            String lastname,
            String email
    ) {}
}
