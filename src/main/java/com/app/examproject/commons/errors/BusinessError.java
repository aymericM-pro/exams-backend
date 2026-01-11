package com.app.examproject.commons.errors;

import org.springframework.http.HttpStatus;

public interface BusinessError {
    String code();
    String message();
    HttpStatus httpStatus();
}
