package com.app.examproject.domains.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "classes")
@Data
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
            mappedBy = "studentClass",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<UserEntity> students = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "class_professors", // snake_case OK
            joinColumns = @JoinColumn(name = "class_id"),
            inverseJoinColumns = @JoinColumn(name = "professor_id")
    )
    private List<UserEntity> professors = new ArrayList<>();

    public ClassEntity() {

    }
}
