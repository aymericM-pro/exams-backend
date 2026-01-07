package com.app.examproject.repositories;

import com.app.examproject.domains.entities.sessions.ExamSessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ExamSessionRepository extends JpaRepository<ExamSessionEntity, UUID> {}
