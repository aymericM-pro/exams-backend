package com.app.examproject.domains.dto.exams;


import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.util.List;

public record CreateExamRequest(
        @NotBlank String title,
        @NotBlank String description,
        @NotBlank String semester,
        @Valid @NotNull List<CreateQuestionRequest> questions
) {}