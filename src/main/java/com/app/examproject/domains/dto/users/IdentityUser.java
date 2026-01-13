package com.app.examproject.domains.dto.users;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class IdentityUser {

    String identityId;
    String email;
    String firstname;
    String lastname;
    boolean enabled;
}