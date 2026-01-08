package com.app.examproject.domains;

import com.app.examproject.domains.dto.users.CreateUserRequest;
import com.app.examproject.domains.dto.users.StudentResponse;
import com.app.examproject.domains.dto.users.UpdateUserRequest;
import com.app.examproject.domains.dto.users.UserResponse;
import com.app.examproject.domains.entities.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "userId", source = "userId")
    @Mapping(
            target = "role",
            expression = "java(entity.getRoles() != null && !entity.getRoles().isEmpty() ? entity.getRoles().iterator().next().name() : null)"
    )
    UserResponse toResponse(UserEntity entity);

    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "exams", ignore = true)
    UserEntity fromCreate(CreateUserRequest request);

    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "exams", ignore = true)
    UserEntity fromUpdate(UpdateUserRequest request);

    @Mapping(target = "id", source = "userId")
    StudentResponse toStudentResponse(UserEntity user);
}
