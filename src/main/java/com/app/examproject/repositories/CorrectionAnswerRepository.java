package com.app.examproject.repositories;

import com.app.examproject.domains.entities.CorrectionAnswerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CorrectionAnswerRepository extends JpaRepository<CorrectionAnswerEntity, UUID> {
}
