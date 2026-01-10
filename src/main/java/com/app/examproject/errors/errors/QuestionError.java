package com.app.examproject.errors.errors;

import com.app.examproject.errors.BusinessError;
import org.springframework.http.HttpStatus;

public enum QuestionError implements BusinessError  {
    QUESTION_NOT_FOUND("QUESTION_404", "Question not found", HttpStatus.NOT_FOUND);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

    QuestionError(String code, String message, HttpStatus httpStatus) {
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
