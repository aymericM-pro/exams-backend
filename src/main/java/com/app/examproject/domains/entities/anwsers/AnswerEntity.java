package com.app.examproject.domains.entities.anwsers;

import com.app.examproject.domains.entities.QuestionEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "answers")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AnswerEntity {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "UUID", name = "answer_id", updatable = false, nullable = false)
    private UUID answerId;

    @Column(nullable = false, length = 2000)
    private String text;

    @Column(nullable = false)
    private boolean correct;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "question_id", nullable = false)
    private QuestionEntity question;

    public AnswerEntity(String text, boolean correct, QuestionEntity question) {
        this.text = text;
        this.correct = correct;
        this.question = question;
    }

}
