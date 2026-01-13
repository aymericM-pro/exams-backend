package com.app.examproject.domains;

import com.app.examproject.domains.dto.users.IdentityUser;
import org.keycloak.representations.idm.UserRepresentation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IdentityUserMapper {

    @Mapping(target = "identityId", source = "id")
    @Mapping(target = "firstname", source = "firstName")
    @Mapping(target = "lastname", source = "lastName")
    IdentityUser toIdentityUser(UserRepresentation user);
}
