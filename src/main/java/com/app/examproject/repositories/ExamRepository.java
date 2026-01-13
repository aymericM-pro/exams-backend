package com.app.examproject.repositories;

import com.app.examproject.domains.entities.ExamEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ExamRepository extends JpaRepository<ExamEntity, UUID> {

    @EntityGraph(
            attributePaths = {
                    "questions",
            }
    )
    Optional<ExamEntity> findExamEntityByExamId(UUID id);
}