package com.app.examproject.services.impl;

import com.app.examproject.commons.errors.BusinessException;
import com.app.examproject.commons.errors.errors.AuthError;
import com.app.examproject.domains.IdentityUserMapper;
import com.app.examproject.domains.UserMapper;
import com.app.examproject.domains.dto.users.IdentityUser;
import com.app.examproject.domains.entities.UserEntity;
import com.app.examproject.repositories.UserRepository;
import com.app.examproject.services.IdentityUserService;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class KeycloakUserServiceImpl implements IdentityUserService {

    private final Keycloak keycloak;
    private final IdentityUserMapper identityUserMapper;
    private final UserMapper userMapper;
    private final UserRepository userRepository;

    @Value("${keycloak.admin.realm}")
    private String realm;

    @Override
    public List<IdentityUser> listUsers() {
        return keycloak
                .realm(realm)
                .users()
                .list()
                .stream()
                .map(this::mapUserWithRoles)
                .toList();
    }

    @Override
    public boolean exists(UUID userId) {
        try {
            var user = keycloak.realm(realm)
                    .users()
                    .get(userId.toString())
                    .toRepresentation();
            return user != null;
        } catch (NotFoundException e) {
            return false;
        }
    }

    @Override
    public void deleteUser(UUID userId) {
        keycloak
                .realm(realm)
                .users()
                .delete(userId.toString());
    }

    public void deleteAllUsers() {
        List<UserRepresentation> users = keycloak
                .realm(realm)
                .users()
                .list();

        for (UserRepresentation user : users) {
            keycloak
                    .realm(realm)
                    .users()
                    .delete(user.getId());
        }
    }

    public void deleteAllUsersAndSessions() {

        // 1. Logout ALL sessions (critique)
        keycloak.realm(realm).logoutAll();

        // 2. Supprimer tous les users (hors service accounts si besoin)
        List<UserRepresentation> users = keycloak
                .realm(realm)
                .users()
                .list();

        for (UserRepresentation user : users) {
            keycloak
                    .realm(realm)
                    .users()
                    .delete(user.getId());
        }
    }

    @Override
    public UserEntity getCurrentUser(Jwt jwt) {

        if (jwt == null || jwt.getSubject() == null) {
            throw new BusinessException(AuthError.USER_NOT_FOUND);
        }

        UUID userId;
        try {
            userId = UUID.fromString(jwt.getSubject());
        } catch (IllegalArgumentException e) {
            throw new BusinessException(AuthError.USER_NOT_FOUND);
        }

        return userRepository
                .findById(UUID.fromString(jwt.getSubject()))
                .orElseThrow(() -> new BusinessException(AuthError.USER_NOT_FOUND));
    }

    private IdentityUser mapUserWithRoles(UserRepresentation user) {

        var roles = keycloak
                .realm(realm)
                .users()
                .get(user.getId())
                .roles()
                .realmLevel()
                .listAll()
                .stream()
                .map(r -> r.getName())
                .collect(Collectors.toSet());

        return identityUserMapper.toIdentityUser(user, roles);
    }
}
