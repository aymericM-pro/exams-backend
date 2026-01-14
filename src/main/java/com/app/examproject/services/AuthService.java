package com.app.examproject.services;

import com.app.examproject.commons.errors.BusinessException;
import com.app.examproject.commons.errors.errors.AuthError;
import com.app.examproject.controller.auth.RegisterRequest;
import com.app.examproject.domains.UserMapper;
import com.app.examproject.domains.dto.users.UserResponse;
import com.app.examproject.domains.entities.Role;
import com.app.examproject.domains.entities.UserEntity;
import com.app.examproject.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.*;

import java.util.List;
import java.util.Set;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final Keycloak keycloak;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    private static final String REALM = "exam";

    public UserResponse register(RegisterRequest request) {

        // 🔒 BLOCK IF USER IS ALREADY AUTHENTICATED
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null
                && auth.isAuthenticated()
                && !(auth instanceof AnonymousAuthenticationToken)) {
            throw new BusinessException(AuthError.USER_ALREADY_AUTHENTICATED);
        }

        UserRepresentation user = new UserRepresentation();
        user.setUsername(request.email());
        user.setEmail(request.email());
        user.setFirstName(request.firstname());
        user.setLastName(request.lastname());
        user.setEnabled(true);

        CredentialRepresentation password = new CredentialRepresentation();
        password.setType(CredentialRepresentation.PASSWORD);
        password.setValue(request.password());
        password.setTemporary(false);
        user.setCredentials(List.of(password));

        var usersResource = keycloak.realm(REALM).users();
        var response = usersResource.create(user);

        if (response.getStatus() == 409) {
            throw new BusinessException(AuthError.USER_ALREADY_EXISTS);
        }

        if (response.getStatus() != 201) {
            throw new BusinessException(AuthError.USER_ALREADY_EXISTS);
        }

        String keycloakUserId =
                response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");

        RoleRepresentation roleRepresentation =
                keycloak.realm(REALM)
                        .roles()
                        .get(request.role())
                        .toRepresentation();

        usersResource
                .get(keycloakUserId)
                .roles()
                .realmLevel()
                .add(List.of(roleRepresentation));

        UserEntity entity = new UserEntity(
                keycloakUserId,
                request.email(),
                Set.of(Role.valueOf("ROLE_" + request.role())),
                request.firstname(),
                request.lastname()
        );

        entity = userRepository.save(entity);

        return userMapper.toResponse(entity);
    }
}

