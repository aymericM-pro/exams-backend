package com.app.examproject.domains.dto.correct;

import java.math.BigDecimal;

public record UpdateAnswerCorrectionRequest(

        Boolean correct,

        BigDecimal score,

        String comment
) {
}
