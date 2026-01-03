package com.app.examproject.repositories;

import com.app.examproject.domains.entities.ClassEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ClassRepository extends JpaRepository<ClassEntity, UUID> {
}