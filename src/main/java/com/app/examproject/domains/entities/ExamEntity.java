package com.app.examproject.domains.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "exams")
public class ExamEntity {

    @Id
    @GeneratedValue
    @Column(name = "exam_id", nullable = false, updatable = false)
    private UUID examId;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, length = 1000)
    private String description;

    @Column(nullable = false, length = 10)
    private String semester;

    @Column(nullable = false)
    private Instant createdAt;

    @OneToMany(
            mappedBy = "exam",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<QuestionEntity> questions = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    public void addQuestion(QuestionEntity q) {
        questions.add(q);
        q.setExam(this);
    }


    public void clearQuestions() {
        for (QuestionEntity q : questions) {
            q.setExam(null);
        }
        questions.clear();
    }
}
