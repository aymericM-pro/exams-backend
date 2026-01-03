package com.app.examproject.domains.dto;

import com.app.examproject.domains.dto.classes.ClassResponse;
import com.app.examproject.domains.dto.classes.CreateClassRequest;
import com.app.examproject.domains.entities.ClassEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ClassMapper {

    ClassResponse toResponse(ClassEntity entity);

    @Mapping(target = "classId", ignore = true)
    @Mapping(target = "students", ignore = true)
    @Mapping(target = "professors", ignore = true)
    ClassEntity fromCreate(CreateClassRequest request);
}