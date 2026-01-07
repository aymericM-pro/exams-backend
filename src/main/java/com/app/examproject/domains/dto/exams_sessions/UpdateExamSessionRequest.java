package com.app.examproject.domains.dto.exams_sessions;

import java.time.Instant;

public record UpdateExamSessionRequest(

        Instant startAt,
        Instant endAt

) {}