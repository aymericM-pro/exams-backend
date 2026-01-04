package com.app.examproject.domains;

import com.app.examproject.domains.dto.classes.ClassResponse;
import com.app.examproject.domains.dto.classes.CreateClassRequest;
import com.app.examproject.domains.entities.ClassEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.app.examproject.domains.entities.UserEntity;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface ClassMapper {

    @Mapping(target = "studentIds", expression = "java(mapStudentIds(entity))")
    @Mapping(target = "professorIds", expression = "java(mapProfessorIds(entity))")
    ClassResponse toResponse(ClassEntity entity);

    @Mapping(target = "classId", ignore = true)
    @Mapping(target = "students", ignore = true)
    @Mapping(target = "professors", ignore = true)
    ClassEntity fromCreate(CreateClassRequest request);

    // ===== Helpers =====

    default List<UUID> mapStudentIds(ClassEntity entity) {
        if (entity.getStudents() == null) return List.of();
        return entity.getStudents()
                .stream()
                .map(UserEntity::getUserId)
                .toList();
    }

    default List<UUID> mapProfessorIds(ClassEntity entity) {
        if (entity.getProfessors() == null) return List.of();
        return entity.getProfessors()
                .stream()
                .map(UserEntity::getUserId)
                .toList();
    }
}