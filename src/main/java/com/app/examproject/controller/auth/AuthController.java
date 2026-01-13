package com.app.examproject.controller.auth;

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
    public UserResponse me(@AuthenticationPrincipal Jwt jwt) {
        return userMapper.toResponse(
                currentUserService.getCurrentUser(jwt)
        );
    }

    @GetMapping("/identity-users")
    public List<IdentityUser> listIdentityUsers() {
        return identityUserService.listUsers();
    }
}
