package com.app.examproject.repositories;

import com.app.examproject.domains.entities.ExamAttemptEntity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ExamAttemptRepository extends JpaRepository<ExamAttemptEntity, UUID> {
    List<ExamAttemptEntity> findByExamSession_ExamSessionId(UUID examSessionId);
}
