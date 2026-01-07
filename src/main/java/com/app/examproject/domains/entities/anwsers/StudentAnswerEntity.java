package com.app.examproject.domains.entities.anwsers;

import com.app.examproject.domains.entities.ExamAttemptEntity;
import com.app.examproject.domains.entities.QuestionEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(
        name = "student_answers",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"exam_attempt_id", "question_id"}
        )
)
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
public abstract class StudentAnswerEntity {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "UUID", updatable = false, nullable = false, name = "student_answer_id")
    private UUID studentAnswerId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "exam_attempt_id", nullable = false)
    protected ExamAttemptEntity examAttempt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "question_id", nullable = false)
    protected QuestionEntity question;

    public abstract void validate();

    public abstract boolean isCorrect();
}
