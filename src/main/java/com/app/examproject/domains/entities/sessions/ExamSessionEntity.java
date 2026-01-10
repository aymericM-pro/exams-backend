package com.app.examproject.domains.entities.sessions;

import com.app.examproject.domains.entities.ClassEntity;
import com.app.examproject.domains.entities.ExamAttemptEntity;
import com.app.examproject.domains.entities.ExamEntity;
import jakarta.persistence.*;

import lombok.*;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "exam_sessions")
public class ExamSessionEntity {

    @Id
    @GeneratedValue
    @Column(name = "exam_session_id", updatable = false, nullable = false, columnDefinition = "UUID")
    private UUID examSessionId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "exam_id", nullable = false)
    private ExamEntity exam;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "class_id", nullable = false)
    private ClassEntity studentClass;

    @Column(nullable = false)
    private Instant startAt;

    @Column(nullable = false)
    private Instant endAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExamSessionStatus status;

    @OneToMany(mappedBy = "examSession", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<ExamAttemptEntity> examAttempts;

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        ExamSessionEntity that = (ExamSessionEntity) obj;
        return examSessionId != null && examSessionId.equals(that.examSessionId);
    }
}
