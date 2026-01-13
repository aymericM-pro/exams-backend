package com.app.examproject.controller.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterRequest(
        @Email
        @NotBlank
        String email,

        @NotBlank
        String password,

        @NotBlank
        String firstname,

        @NotBlank
        String lastname,

        @NotBlank
        String role
) {}
