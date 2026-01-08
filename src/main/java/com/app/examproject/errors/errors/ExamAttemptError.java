package com.app.examproject.errors.errors;

import com.app.examproject.errors.BusinessError;
import org.springframework.http.HttpStatus;

public enum ExamAttemptError implements BusinessError {

    EXAM_ATTEMPT_NOT_FOUND("EXAM_ATTEMPT_404", "Exam attempt not found", HttpStatus.NOT_FOUND),
    INVALID_REQUEST("EXAM_ATTEMPT_400", "Invalid exam attempt request", HttpStatus.BAD_REQUEST),
    EXAM_ATTEMPT_ALREADY_SUBMITTED("EXAM_ATTEMPT_409", "Exam attempt already submitted", HttpStatus.CONFLICT);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

    ExamAttemptError(String code, String message, HttpStatus httpStatus) {
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
