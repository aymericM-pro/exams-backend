package com.app.examproject.errors.errors;

import com.app.examproject.errors.BusinessError;
import org.springframework.http.HttpStatus;

public enum ClassError implements BusinessError {

    CLASS_NOT_FOUND("CLASS_404", "Class not found", HttpStatus.NOT_FOUND),
    INVALID_REQUEST("CLASS_400", "Invalid class request", HttpStatus.BAD_REQUEST);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

    ClassError(String code, String message, HttpStatus httpStatus) {
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