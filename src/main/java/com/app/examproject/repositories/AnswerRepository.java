package com.app.examproject.repositories;

import com.app.examproject.domains.entities.anwsers.AnswerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AnswerRepository extends JpaRepository<AnswerEntity, UUID> {
}