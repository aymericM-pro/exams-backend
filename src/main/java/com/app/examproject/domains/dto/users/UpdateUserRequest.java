package com.app.examproject.domains.dto.users;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UpdateUserRequest(

    @Email
    String email,

    String firstName,

    String lastName,

    String role){
}