package com.app.examproject.domains.dto.users;

import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class UserResponse {

    UUID userId;
    String email;
    String firstname;
    String lastname;
    String role;
}