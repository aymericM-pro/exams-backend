package com.app.examproject.domains.dto.classes;

import java.util.UUID;
import java.util.List;

public record ClassResponse(
        UUID classId,
        String name,
        String graduationYear,
        List<UUID> studentIds,
        List<UUID> professorIds
) {
}