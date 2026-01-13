package com.app.examproject.services;

import com.app.examproject.domains.dto.users.IdentityUser;

import java.util.List;

public interface IdentityUserService {
    List<IdentityUser> listUsers();
}