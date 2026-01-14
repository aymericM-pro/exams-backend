package com.app.examproject.domains;

import com.app.examproject.domains.dto.users.IdentityUser;
import org.keycloak.representations.idm.UserRepresentation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface IdentityUserMapper {

    @Mapping(target = "identityId", source = "user.id")
    @Mapping(target = "firstname", source = "user.firstName")
    @Mapping(target = "lastname", source = "user.lastName")
    @Mapping(target = "roles", source = "roles")
    IdentityUser toIdentityUser(UserRepresentation user, Set<String> roles);
}
