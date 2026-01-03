package com.app.examproject.domains.dto.classes;

import java.util.UUID;

public record ClassResponse(
        UUID classId,
        String name,
        String graduationYear
) {
}