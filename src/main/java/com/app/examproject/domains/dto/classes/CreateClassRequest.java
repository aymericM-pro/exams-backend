package com.app.examproject.domains.dto.classes;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;
import java.util.UUID;

public record CreateClassRequest(

        @NotBlank(message = "Class name must not be blank")
        String name,

        @NotBlank(message = "Graduation year must not be blank")
        String graduationYear,

        @NotEmpty(message = "Student IDs list must not be empty")
        List<UUID> studentIds,

        @NotEmpty(message = "Professor IDs list must not be empty")
        List<UUID> professorIds) {
}