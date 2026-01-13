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
                .map(identityUserMapper::toIdentityUser)
                .toList();
    }
}
