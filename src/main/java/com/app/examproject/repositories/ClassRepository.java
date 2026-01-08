package com.app.examproject.repositories;

import com.app.examproject.domains.entities.ClassEntity;
import com.app.examproject.domains.entities.UserEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ClassRepository extends JpaRepository<ClassEntity, UUID> {

    @EntityGraph(attributePaths = "students")
    Optional<ClassEntity> findWithStudentsByClassId(UUID classId);
}