package com.app.examproject.domains.entities.anwsers;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "student_single_answers")
@Getter
@Setter
public class StudentSingleAnswerEntity extends StudentAnswerEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "answer_id")
    private AnswerEntity selectedAnswer;

    @Override
    public void validate() {
        if (selectedAnswer == null) {
            throw new IllegalStateException("Single answer must be selected");
        }
    }

    @Override
    public boolean isCorrect() {
        return selectedAnswer.isCorrect();
    }
}