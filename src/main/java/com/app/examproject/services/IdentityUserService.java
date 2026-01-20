package com.app.examproject.services;

import com.app.examproject.domains.dto.users.IdentityUser;
import com.app.examproject.domains.entities.UserEntity;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;

import java.util.UUID;

public interface IdentityUserService {
    List<IdentityUser> listUsers();
    boolean exists(UUID userId);
    UserEntity getCurrentUser(Jwt jwt);
    void deleteUser(UUID userId);
    void deleteAllUsers();
    void deleteAllUsersAndSessions();
}