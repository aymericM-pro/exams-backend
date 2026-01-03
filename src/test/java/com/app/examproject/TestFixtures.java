package com.app.examproject;

import com.app.examproject.domains.dto.exams.*;
import com.app.examproject.domains.dto.users.CreateUserRequest;
import com.app.examproject.domains.dto.users.UpdateUserRequest;
import com.app.examproject.domains.dto.users.UserResponse;
import com.app.examproject.domains.entities.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class TestFixtures {


    public static CreateExamRequest createExamRequest() {
        return new CreateExamRequest(
                "Exam",
                "Description",
                "S1",
                List.of(createQuestionRequest())
        );
    }

    public static CreateQuestionRequest createQuestionRequest() {
        return new CreateQuestionRequest(
                "Question 1",
                "single",
                List.of(
                        new CreateAnswerRequest("Answer A", true),
                        new CreateAnswerRequest("Answer B", false)
                )
        );
    }

    public static UpdateExamRequest updateExamRequest() {
        return new UpdateExamRequest(
                "Updated",
                "Updated desc",
                "S2",
                List.of(updateQuestionRequest())
        );
    }

    public static UpdateQuestionRequest updateQuestionRequest() {
        return new UpdateQuestionRequest(
                UUID.randomUUID(),
                "Question 1",
                "single",
                List.of(
                        new UpdateAnswerRequest(
                                UUID.randomUUID(),
                                "Answer A",
                                true
                        ),
                        new UpdateAnswerRequest(
                                UUID.randomUUID(),
                                "Answer B",
                                false
                        )
                )
        );
    }

    public static ExamEntity examEntity() {
        ExamEntity exam = new ExamEntity();
        exam.setExamId(UUID.randomUUID());
        exam.setTitle("Exam");
        exam.setDescription("Desc");
        exam.setSemester("S1");
        exam.setCreatedAt(Instant.now());

        QuestionEntity q = new QuestionEntity("Q1", QuestionType.SINGLE);
        q.addAnswer(new AnswerEntity("Answer A", true));
        q.addAnswer(new AnswerEntity("Answer B", false));

        exam.addQuestion(q);
        return exam;
    }

    public static ExamResponse examResponse() {
        return new ExamResponse(
                UUID.randomUUID(),
                "Exam",
                "Desc",
                "S1",
                Instant.now(),
                List.of()
        );
    }

    public static UserEntity userEntity(UUID userId) {
        UserEntity user = new UserEntity();
        user.setUserId(userId);
        user.setEmail("test@test.com");
        user.setPassword("password");
        return user;
    }

    public static CreateUserRequest createUserRequest() {
        return new CreateUserRequest(
                UUID.randomUUID() + "@test.com",
                "password123",
                "USER",
                "John",
                "ROLE_STUDENT"
        );
    }

    public static UserResponse userResponse(UUID userId) {
        return UserResponse.builder()
                .userId(userId)
                .email("test@test.com")
                .firstname("John")
                .lastname("Doe")
                .role("ROLE_STUDENT")
                .build();
    }

    public static UpdateUserRequest updateUserRequest() {
        return new UpdateUserRequest(
                "updated@test.com",
                "newPassword123",
                "John",
                "Doe",
                "ROLE_STUDENT"
        );
    }
}
