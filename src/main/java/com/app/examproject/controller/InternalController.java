package com.app.examproject.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/internal")
public class InternalController {

    @GetMapping("/whoami")
    public Map<String, Object> whoAmI(Authentication authentication) {
        JwtAuthenticationToken token = (JwtAuthenticationToken) authentication;
        Jwt jwt = token.getToken();

        Map<String, Object> resourceAccess = jwt.getClaim("resource_access");

        List<String> roles = List.of();
        if (resourceAccess != null && resourceAccess.containsKey("exam-backend")) {
            Map<String, Object> clientAccess =
                    (Map<String, Object>) resourceAccess.get("exam-backend");
            roles = (List<String>) clientAccess.getOrDefault("roles", List.of());
        }

        return Map.of(
                "subject", jwt.getSubject(),
                "client_id", jwt.getClaimAsString("client_id"),
                "roles", roles
        );
    }

}