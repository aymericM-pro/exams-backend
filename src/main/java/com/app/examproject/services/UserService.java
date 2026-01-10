package com.app.examproject.services;


import com.app.examproject.domains.dto.users.CreateUserRequest;
import com.app.examproject.domains.dto.users.UpdateUserRequest;
import com.app.examproject.domains.dto.users.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface UserService {
    UserResponse create(CreateUserRequest request);
    List<UserResponse> createMany(List<CreateUserRequest> requests);
    Page<UserResponse> getAll(Pageable pageable);
    UserResponse getById(UUID id);
    UserResponse update(UUID id, UpdateUserRequest request);
    void delete(UUID id);
}
