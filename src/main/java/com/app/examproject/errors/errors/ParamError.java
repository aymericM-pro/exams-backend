package com.app.examproject.errors.errors;

import com.app.examproject.errors.BusinessError;
import org.springframework.http.HttpStatus;

public enum ParamError implements BusinessError {

    INVALID(HttpStatus.BAD_REQUEST),
    MISSING(HttpStatus.BAD_REQUEST),
    FORMAT(HttpStatus.BAD_REQUEST),
    RANGE(HttpStatus.BAD_REQUEST);

    private final HttpStatus status;

    ParamError(HttpStatus status) {
        this.status = status;
    }

    @Override
    public String code() {
        return "PARAM_" + name();
    }

    @Override
    public String message() {
        return "Invalid request parameter";
    }

    @Override
    public HttpStatus httpStatus() {
        return status;
    }
}
