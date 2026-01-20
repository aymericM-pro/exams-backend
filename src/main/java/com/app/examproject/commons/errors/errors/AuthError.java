package com.app.examproject.commons.errors.errors;

import com.app.examproject.commons.errors.BusinessError;
import org.springframework.http.HttpStatus;

public enum AuthError implements BusinessError {

    USER_CREATION_FAILED("AUTH_500_USER_CREATION_FAILED", "User creation failed", HttpStatus.INTERNAL_SERVER_ERROR),
    USER_NOT_FOUND("AUTH_404_USER_NOT_FOUND", "User not found", HttpStatus.NOT_FOUND),
    USER_ALREADY_AUTHENTICATED("AUTH_403_ALREADY_AUTHENTICATED", "User is already authenticated", HttpStatus.FORBIDDEN),
    USER_ALREADY_EXISTS("AUTH_409_ALREADY_EXISTS", "User already exists", HttpStatus.CONFLICT),
    KEYCLOAK_CREATION_FAILED("AUTH_500_KEYCLOAK_ERROR", "Keycloak user creation failed", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

    AuthError(String code, String message, HttpStatus httpStatus) {
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
