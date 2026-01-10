package com.app.examproject.repositories;

import com.app.examproject.domains.entities.ExamAttemptEntity;
import com.app.examproject.domains.entities.anwsers.StudentAnswerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface StudentAnswerRepository
        extends JpaRepository<StudentAnswerEntity, UUID> {

    void deleteByExamAttempt(ExamAttemptEntity examAttempt);
}