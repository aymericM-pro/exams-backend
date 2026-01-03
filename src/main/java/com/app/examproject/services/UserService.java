package com.app.examproject.services;


import com.app.examproject.domains.dto.users.CreateUserRequest;
import com.app.examproject.domains.dto.users.UpdateUserRequest;
import com.app.examproject.domains.dto.users.UserResponse;

import java.util.List;
import java.util.UUID;

public interface UserService {
    UserResponse create(CreateUserRequest request);
    List<UserResponse> getAll();
    UserResponse getById(UUID id);
    UserResponse update(UUID id, UpdateUserRequest request);
    void delete(UUID id);

}
