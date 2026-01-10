package com.app.examproject.controller.users;

import com.app.examproject.domains.dto.users.CreateUserRequest;
import com.app.examproject.domains.dto.users.UpdateUserRequest;
import com.app.examproject.domains.dto.users.UserResponse;
import com.app.examproject.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController implements IUserControllerSwagger {

    private final UserService userService;

    @Override
    @PostMapping
    public ResponseEntity<UserResponse> create(CreateUserRequest request) {
        UserResponse response = userService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    @PostMapping("/many")
    public ResponseEntity<List<UserResponse>> createMany(List<CreateUserRequest> request) {
        List<UserResponse> response = userService.createMany(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAll() {
        return ResponseEntity.ok(userService.getAll());
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getById(UUID id) {
        return ResponseEntity.ok(userService.getById(id));
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> update(UUID id, UpdateUserRequest request) {
        return ResponseEntity.ok(userService.update(id, request));
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(UUID id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
