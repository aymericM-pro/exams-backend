package com.app.examproject.commons.errors.errors;

import com.app.examproject.commons.errors.BusinessError;
import org.springframework.http.HttpStatus;

public enum ExamAttemptError implements BusinessError {
    STUDENT_ANSWER_NOT_FOUND("STUDENT_ANSWER_404", "Student answer not found", HttpStatus.NOT_FOUND),
    EXAM_ATTEMPT_NOT_SUBMITTED("EXAM_ATTEMPT_400", "Exam attempt has not been submitted yet", HttpStatus.BAD_REQUEST),
    EXAM_ATTEMPT_NOT_FOUND("EXAM_ATTEMPT_404", "Exam attempt not found", HttpStatus.NOT_FOUND),
    EXAM_ATTEMPT_ALREADY_EXISTS("EXAM_ATTEMPT_409", "Exam attempt already exists for this user and session", HttpStatus.CONFLICT),
    USER_NOT_ALLOWED_FOR_SESSION("EXAM_ATTEMPT_403", "User not allowed for this exam session", HttpStatus.FORBIDDEN),
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
