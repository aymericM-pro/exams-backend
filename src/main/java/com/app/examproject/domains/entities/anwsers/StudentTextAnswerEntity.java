package com.app.examproject.domains.entities.anwsers;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "student_text_answers")
@Getter
@Setter
public class StudentTextAnswerEntity extends StudentAnswerEntity {

    @Column(nullable = false, length = 2000)
    private String content;

    @Override
    public void validate() {
        if (content == null || content.isBlank()) {
            throw new IllegalStateException("Text answer cannot be empty");
        }
    }

    @Override
    public boolean isCorrect() {
        return false;
    }
}