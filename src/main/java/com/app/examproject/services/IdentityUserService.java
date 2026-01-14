package com.app.examproject.services;

import com.app.examproject.domains.dto.users.IdentityUser;

import java.util.List;

import java.util.UUID;

public interface IdentityUserService {
    List<IdentityUser> listUsers();
    void deleteUser(UUID userId);
    void deleteAllUsers();
}