package com.app.examproject.domains.entities;

import jakarta.persistence.*;
import java.util.*;

@Entity
@Table(name = "questions")
public class QuestionEntity {

    @Id
    @GeneratedValue
    @Column(name = "question_id", updatable = false, nullable = false)
    private UUID questionId;

    @Column(nullable = false, length = 2000)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private QuestionType type;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "exam_id", nullable = false)
    private ExamEntity exam;

    @OneToMany(
            mappedBy = "question",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<AnswerEntity> answers = new ArrayList<>();

    public QuestionEntity() {}

    public QuestionEntity(String title, QuestionType type) {
        this.title = title;
        this.type = type;
    }

    // getters/setters
    public UUID getId() { return questionId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public QuestionType getType() { return type; }
    public void setType(QuestionType type) { this.type = type; }

    public ExamEntity getExam() { return exam; }
    public void setExam(ExamEntity exam) { this.exam = exam; }

    public List<AnswerEntity> getAnswers() { return answers; }

    public void setAnswers(List<AnswerEntity> answers) {
        this.answers.clear();
        if (answers != null) {
            answers.forEach(this::addAnswer);
        }
    }

    public void addAnswer(AnswerEntity a) {
        a.setQuestion(this);
        this.answers.add(a);
    }
}
