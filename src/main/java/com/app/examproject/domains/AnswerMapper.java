package com.app.examproject.domains;

import com.app.examproject.domains.dto.exams.AnswerResponse;
import com.app.examproject.domains.dto.exams.CreateAnswerRequest;
import com.app.examproject.domains.dto.exams.UpdateAnswerRequest;
import com.app.examproject.domains.entities.AnswerEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AnswerMapper {

    AnswerResponse toResponse(AnswerEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "question", ignore = true)
    AnswerEntity fromCreate(CreateAnswerRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "question", ignore = true)
    AnswerEntity fromUpdate(UpdateAnswerRequest request);
}