package com.app.examproject.commons.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class SecurityExpressionService {

    public boolean check(String requiredRole) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null) {
            return false;
        }

        String expectedAuthority = normalize(requiredRole);

        return auth.getAuthorities().stream()
                .anyMatch(a -> Objects.equals(a.getAuthority(), expectedAuthority));
    }

    private String normalize(String role) {
        return role.startsWith("ROLE_") ? role : "ROLE_" + role;
    }
}
