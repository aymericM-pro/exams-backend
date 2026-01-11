package com.app.examproject.domains.dto.correct;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateAnswerCorrectionRequest(

        @NotNull
        UUID studentAnswerId,

        @NotNull
        Boolean correct,

        Double score,

        String comment
) {
}
