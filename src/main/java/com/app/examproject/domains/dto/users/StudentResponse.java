package com.app.examproject.domains.dto.users;


import java.util.UUID;

public record StudentResponse(
        UUID id,
        String firstname,
        String lastname,
        String email
) {}