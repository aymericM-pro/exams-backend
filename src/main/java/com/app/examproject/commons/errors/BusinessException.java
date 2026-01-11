package com.app.examproject.commons.errors;


import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    private final BusinessError error;

    public BusinessException(BusinessError error) {
        super(error.message());
        this.error = error;
    }

    public BusinessError getError() {
        return error;
    }
}
