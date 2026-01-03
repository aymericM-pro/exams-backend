package com.app.examproject.domains;

import com.app.examproject.domains.dto.exams.CreateQuestionRequest;
import com.app.examproject.domains.dto.exams.QuestionResponse;
import com.app.examproject.domains.dto.exams.UpdateQuestionRequest;
import com.app.examproject.domains.entities.QuestionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        componentModel = "spring",
        uses = AnswerMapper.class
)
public interface QuestionMapper {

    @Mapping(target = "type", expression = "java(entity.getType().name().toLowerCase())")
    QuestionResponse toResponse(QuestionEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "exam", ignore = true)
    @Mapping(
            target = "type",
            expression = "java(com.app.examproject.domains.entities.QuestionType.valueOf(request.type().toUpperCase()))"
    )
    QuestionEntity fromCreate(CreateQuestionRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "exam", ignore = true)
    @Mapping(
            target = "type",
            expression = "java(com.app.examproject.domains.entities.QuestionType.valueOf(request.type().toUpperCase()))"
    )
    QuestionEntity fromUpdate(UpdateQuestionRequest request);
}
