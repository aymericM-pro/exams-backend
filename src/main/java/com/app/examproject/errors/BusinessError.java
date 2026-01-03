package com.app.examproject.errors;

import org.springframework.http.HttpStatus;

public interface BusinessError {
    String code();
    String message();
    HttpStatus httpStatus();
}
