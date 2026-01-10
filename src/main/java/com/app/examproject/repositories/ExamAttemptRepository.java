package com.app.examproject.repositories;

import com.app.examproject.domains.entities.ExamAttemptEntity;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ExamAttemptRepository extends JpaRepository<ExamAttemptEntity, UUID> {
    List<ExamAttemptEntity> findByExamSession_ExamSessionId(UUID examSessionId);

    Optional<ExamAttemptEntity> findByExamSession_ExamSessionIdAndStudent_UserId(UUID sessionId, UUID userId);
    boolean existsByExamSession_ExamSessionIdAndStudent_UserId(UUID sessionId, UUID userId);


    @EntityGraph(attributePaths = {
            "examSession",
            "examSession.exam",
            "examSession.exam.questions",
            "answers"
    })
    Optional<ExamAttemptEntity> findByExamAttemptId(UUID attemptId);

}
