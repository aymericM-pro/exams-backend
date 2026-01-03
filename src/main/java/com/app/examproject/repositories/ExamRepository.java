package com.app.examproject.repositories;

import com.app.examproject.domains.entities.ExamEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ExamRepository extends JpaRepository<ExamEntity, UUID> {
}