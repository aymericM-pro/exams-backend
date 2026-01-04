package com.app.examproject.domains.dto.classes;

import java.util.List;
import java.util.UUID;

public record CreateClassRequest(
        String name,
        String graduationYear,
        List<UUID> studentIds,
        List<UUID> professorIds
) {
}