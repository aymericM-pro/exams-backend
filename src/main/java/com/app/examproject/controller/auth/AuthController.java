package com.app.examproject.controller.auth;

import com.app.examproject.commons.security.Require;
import com.app.examproject.domains.UserMapper;
import com.app.examproject.domains.dto.users.IdentityUser;
import com.app.examproject.domains.dto.users.UserResponse;
import com.app.examproject.services.AuthService;
import com.app.examproject.services.CurrentUserService;
import com.app.examproject.services.IdentityUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final CurrentUserService currentUserService;
    private final UserMapper userMapper;
    private final IdentityUserService identityUserService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(
            @RequestBody RegisterRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(authService.register(request));
    }

    @GetMapping("/me")
    @Require("PROF")
    public UserResponse me(@AuthenticationPrincipal Jwt jwt) {
        return userMapper.toResponse(
                currentUserService.getCurrentUser(jwt)
        );
    }

    @GetMapping("/identity-users")
    public List<IdentityUser> listIdentityUsers() {
        return identityUserService.listUsers();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID userId) {
        identityUserService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllUsers() {
        identityUserService.deleteAllUsers();
        return ResponseEntity.noContent().build();
    }
}
