package com.app.examproject.commons.errors.errors;

import com.app.examproject.commons.errors.BusinessError;
import org.springframework.http.HttpStatus;

public enum AnswerError implements BusinessError {
    MULTIPLE_ANSWERS_REQUIRED("ANSWER_400", "Multiple answers required", HttpStatus.BAD_REQUEST),
    TEXT_ANSWER_REQUIRED("ANSWER_400", "Add text Abswer required", HttpStatus.BAD_REQUEST),
    SINGLE_ANSWER_REQUIRED("ANSWER_400", "Single answers required", HttpStatus.BAD_REQUEST),
    ANSWER_NOT_FOUND("ANSWER_404", "Answer not found", HttpStatus.NOT_FOUND),;

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

    AnswerError(String code, String message, HttpStatus httpStatus) {
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
