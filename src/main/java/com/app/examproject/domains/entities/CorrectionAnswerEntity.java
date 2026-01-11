package com.app.examproject.domains.entities;

import com.app.examproject.domains.entities.anwsers.StudentAnswerEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "correct_answers")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CorrectionAnswerEntity {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "UUID", updatable = false, unique = true)
    private UUID correctAnswerId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_answer_id", nullable = false, unique = true)
    private StudentAnswerEntity studentAnswer;

    private boolean correct;
    private BigDecimal score;

    @Column(length = 2000)
    private String comment;

    private UUID correctedBy;
    private Instant correctedAt;
}
