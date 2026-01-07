package com.app.examproject.domains.entities.anwsers;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@Table(name = "student_multiple_answers")
@Getter
@Setter
public class StudentMultipleAnswerEntity extends StudentAnswerEntity {

    @ManyToMany
    @JoinTable(
            name = "student_multiple_answer_links",
            joinColumns = @JoinColumn(name = "student_answer_id"),
            inverseJoinColumns = @JoinColumn(name = "answer_id")
    )
    private Set<AnswerEntity> selectedAnswers = new HashSet<>();

    @Override
    public void validate() {
        if (selectedAnswers == null || selectedAnswers.isEmpty()) {
            throw new IllegalStateException("At least one answer must be selected");
        }
    }

    @Override
    public boolean isCorrect() {
        Set<UUID> expected = question.getAnswers().stream()
                .filter(AnswerEntity::isCorrect)
                .map(AnswerEntity::getAnswerId)
                .collect(Collectors.toSet());

        Set<UUID> actual = selectedAnswers.stream()
                .map(AnswerEntity::getAnswerId)
                .collect(Collectors.toSet());

        return expected.equals(actual);
    }
}
