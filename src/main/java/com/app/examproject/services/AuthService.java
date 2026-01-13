package com.app.examproject.services;

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

@Service
@RequiredArgsConstructor
public class AuthService {

    private final Keycloak keycloak;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    private static final String REALM = "exam";
    private static final String CLIENT_ID = "exam-backend";

    public UserResponse register(RegisterRequest request) {

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
            throw new IllegalStateException("User already exists in Keycloak");
        }

        if (response.getStatus() != 201) {
            throw new IllegalStateException(
                    "Keycloak user creation failed with status " + response.getStatus()
            );
        }

        // 🔹 EXTRACT KEYCLOAK USER ID
        // The user ID is part of the 'Location' header in the response
        // e.g., Location: http://<keycloak-server>/auth/admin/realms/{realm}/users/{id}
        // We extract the {id} part using a regex

        String keycloakUserId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");

        // 🔹 ASSIGN REALM ROLE
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
