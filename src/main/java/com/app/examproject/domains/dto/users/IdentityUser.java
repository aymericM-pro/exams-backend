package com.app.examproject.domains.dto.users;

import lombok.Builder;
import lombok.Value;

import java.util.Set;

@Value
@Builder
public class IdentityUser {

    String identityId;
    String email;
    String firstname;
    String lastname;
    boolean enabled;
    Set<String> roles;

}