package com.app.examproject.domains.dto.classes;

import java.util.UUID;

public record DeleteClassRequest(
        UUID classId
) {
}