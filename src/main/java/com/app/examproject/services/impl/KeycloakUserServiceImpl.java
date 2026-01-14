package com.app.examproject.services.impl;

import com.app.examproject.domains.IdentityUserMapper;
import com.app.examproject.domains.dto.users.IdentityUser;
import com.app.examproject.services.IdentityUserService;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class KeycloakUserServiceImpl implements IdentityUserService {

    private final Keycloak keycloak;
    private final IdentityUserMapper identityUserMapper;

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
