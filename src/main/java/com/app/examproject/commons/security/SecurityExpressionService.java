package com.app.examproject.commons.security;

import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityExpressionService {

    private static final org.slf4j.Logger log =
            LoggerFactory.getLogger(SecurityExpressionService.class);

    public boolean check(String requiredRole) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null) {
            return false;
        }

        String expectedAuthority = normalize(requiredRole);

        boolean match = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(expectedAuthority));

        return match;
    }

    private String normalize(String role) {
        return role.startsWith("ROLE_") ? role : "ROLE_" + role;
    }
}
