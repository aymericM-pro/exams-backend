package com.app.examproject.domains.entities;

import com.app.examproject.domains.entities.anwsers.AnswerEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

@Entity
@Table(name = "questions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

    public QuestionEntity(String title, QuestionType type) {
        this.title = title;
        this.type = type;
    }
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        QuestionEntity that = (QuestionEntity) o;
        return Objects.equals(questionId, that.questionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(questionId);
    }
}
