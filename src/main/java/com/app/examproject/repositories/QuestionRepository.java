package com.app.examproject.repositories;

import com.app.examproject.domains.entities.QuestionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface QuestionRepository extends JpaRepository<QuestionEntity, UUID> {
}
