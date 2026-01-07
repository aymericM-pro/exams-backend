package com.app.examproject.domains.dto.exams_sessions;

import com.app.examproject.domains.entities.sessions.ExamSessionStatus;

import java.time.Instant;
import java.util.UUID;

public record ExamSessionResponse(

        UUID examSessionId,

        UUID examId,
        String examTitle,

        UUID classId,
        String className,

        Instant startAt,
        Instant endAt,

        ExamSessionStatus status,

        int attemptsCount

) {}