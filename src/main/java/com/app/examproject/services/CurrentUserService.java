package com.app.examproject.services;

import com.app.examproject.domains.entities.UserEntity;
import com.app.examproject.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CurrentUserService {

    private final UserRepository userRepository;

    public UserEntity getCurrentUser(Jwt jwt) {
        String keycloakUserId = jwt.getSubject();

        return userRepository.findByKeycloakUserId(keycloakUserId)
                .orElseThrow(() ->
                        new IllegalStateException("User not found for Keycloak ID " + keycloakUserId)
                );
    }
}