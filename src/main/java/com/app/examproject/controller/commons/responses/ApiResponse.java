package com.app.examproject.controller.commons.responses;

import java.time.Instant;

public record ApiResponse<T>(
        boolean success,
        T data,
        Object meta,
        Instant timestamp
) {
    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(true, data, null, Instant.now());
    }

    public static <T> ApiResponse<T> ok(T data, Object meta) {
        return new ApiResponse<>(true, data, meta, Instant.now());
    }
}
