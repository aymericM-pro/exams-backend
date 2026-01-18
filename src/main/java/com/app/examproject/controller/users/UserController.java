package com.app.examproject.controller.users;

import com.app.examproject.domains.dto.users.CreateUserRequest;
import com.app.examproject.domains.dto.users.UpdateUserRequest;
import com.app.examproject.domains.dto.users.UserResponse;
import com.app.examproject.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    @GetMapping("/search")
    public ResponseEntity<Page<UserResponse>> search(
            @ParameterObject UserSearchParams params,
            @ParameterObject Pageable pageable
    ) {
        params.formalize();

        return ResponseEntity.ok(
                userService.search(params, pageable)
        );
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.getById(id));
    }

    @Override
    @GetMapping
    public ResponseEntity<Page<UserResponse>> getAll(
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(userService.getAll(pageable));
    }

    @Override
    @PostMapping
    public ResponseEntity<UserResponse> create(
            @RequestBody CreateUserRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userService.create(request));
    }

    @Override
    @PostMapping("/batch")
    public ResponseEntity<List<UserResponse>> createMany(
            @RequestBody List<CreateUserRequest> requests
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userService.createMany(requests));
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> update(
            @PathVariable UUID id,
            @RequestBody UpdateUserRequest request
    ) {
        return ResponseEntity.ok(userService.update(id, request));
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
