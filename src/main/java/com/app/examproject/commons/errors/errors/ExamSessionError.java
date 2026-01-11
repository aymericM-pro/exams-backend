package com.app.examproject.commons.errors.errors;

import com.app.examproject.commons.errors.BusinessError;
import org.springframework.http.HttpStatus;

public enum ExamSessionError implements BusinessError {

    EXAM_SESSION_NOT_FOUND("EXAM_SESSION_404", "Exam session not found", HttpStatus.NOT_FOUND),
    EXAM_NOT_FOUND("EXAM_404", "Exam not found", HttpStatus.NOT_FOUND),
    CLASS_NOT_FOUND("CLASS_404", "Class not found", HttpStatus.NOT_FOUND);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

    ExamSessionError(String code, String message, HttpStatus httpStatus) {
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
