package com.app.examproject.domains.dto.correct;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;
import java.util.UUID;

public record CreateExamCorrectionRequest(

        @NotEmpty
        List<QuestionCorrectionRequest> questions

) {}
