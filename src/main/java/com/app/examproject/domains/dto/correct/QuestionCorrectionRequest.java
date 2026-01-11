package com.app.examproject.domains.dto.correct;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public record QuestionCorrectionRequest(

        @NotNull
        UUID questionId,

        Boolean manuallyCorrect,

        String comment

) {}
