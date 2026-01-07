package com.app.examproject.repositories;

import com.app.examproject.domains.entities.ExamAttemptEntity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ExamAttemptRepository extends JpaRepository<ExamAttemptEntity, UUID> {
}
