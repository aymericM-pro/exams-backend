package com.app.examproject.domains.entities;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "answers")
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

    public AnswerEntity() {}

    public AnswerEntity(String text, boolean correct) {
        this.text = text;
        this.correct = correct;
    }

    public UUID getId() { return answerId; }
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
    public boolean isCorrect() { return correct; }
    public void setCorrect(boolean correct) { this.correct = correct; }

    public QuestionEntity getQuestion() { return question; }
    public void setQuestion(QuestionEntity question) { this.question = question; }
}
