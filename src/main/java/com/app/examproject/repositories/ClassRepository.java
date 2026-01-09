package com.app.examproject.repositories;

import com.app.examproject.domains.entities.ClassEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ClassRepository extends JpaRepository<ClassEntity, UUID> {

    @EntityGraph(attributePaths = "students")
    Optional<ClassEntity> findWithStudentsByClassId(UUID classId);
}