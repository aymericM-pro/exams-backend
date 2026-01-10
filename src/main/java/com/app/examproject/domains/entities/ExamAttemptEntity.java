package com.app.examproject.domains.entities;

import com.app.examproject.domains.entities.anwsers.StudentAnswerEntity;
import com.app.examproject.domains.entities.sessions.ExamSessionEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "exam_attempts")
public class ExamAttemptEntity {

    @Id
    @GeneratedValue
    @Column(name = "attempt_id", updatable = false, nullable = false)
    private UUID examAttemptId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "exam_session_id", nullable = false)
    private ExamSessionEntity examSession;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity student;

    @OneToMany(
            mappedBy = "examAttempt",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<StudentAnswerEntity> answers = new HashSet<>();

    @Column(nullable = false)
    private Instant startedAt;

    @Column
    private Instant submittedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof ExamAttemptEntity that)) {
            return false;
        }

        return examAttemptId != null && examAttemptId.equals(that.getExamAttemptId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
