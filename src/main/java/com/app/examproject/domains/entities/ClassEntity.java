package com.app.examproject.domains.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "classes")
public class ClassEntity {

    @Id
    @GeneratedValue
    @Column(name = "class_id", nullable = false, updatable = false)
    private UUID classId;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "graduation_year", length = 50)
    private String graduationYear;

    @OneToMany(
            mappedBy = "studentClass"
    )
    private List<UserEntity> students = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "class_professors",
            joinColumns = @JoinColumn(name = "class_id"),
            inverseJoinColumns = @JoinColumn(name = "professor_id")
    )
    private List<UserEntity> professors = new ArrayList<>();

    public void addStudent(UserEntity student) {
        students.add(student);
        student.setStudentClass(this);
    }

    public void removeStudent(UserEntity student) {
        students.remove(student);
        student.setStudentClass(null);
    }

    public void addProfessor(UserEntity professor) {
        professors.add(professor);
    }

    public void removeProfessor(UserEntity professor) {
        professors.remove(professor);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ClassEntity that = (ClassEntity) o;

        return classId != null ? classId.equals(that.classId) : that.classId == null;
    }

    @Override
    public int hashCode() {
        return classId != null ? classId.hashCode() : 0;
    }
}
