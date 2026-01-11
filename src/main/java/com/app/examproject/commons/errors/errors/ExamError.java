package com.app.examproject.commons.errors.errors;

import com.app.examproject.commons.errors.BusinessError;
import org.springframework.http.HttpStatus;

public enum ExamError implements BusinessError {
    EXAM_NOT_FOUND("EXAM_404", "Exam not found", HttpStatus.NOT_FOUND),
    INVALID_REQUEST("EXAM_400", "Invalid exam request", HttpStatus.BAD_REQUEST);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

    ExamError(String code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    @Override
    public String code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }

    @Override
    public HttpStatus httpStatus() {
        return httpStatus;
    }
}
