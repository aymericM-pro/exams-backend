package com.app.examproject.domains.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "users")
@Data
public class UserEntity {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "UUID", updatable = false, nullable = false, name = "user_id")
    private UUID userId;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, name = "first_name")
    private String firstname;

    @Column(nullable = false, name = "last_name")
    private String lastname;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id")
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Set<Role> roles;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "users_exams",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "exam_id")
    )
    private Set<ExamEntity> exams = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_id")
    private ClassEntity studentClass;

    public UserEntity() {}

    public UserEntity(String email, String password, Set<Role> roles, String firstname, String lastname) {
        this.email = email;
        this.password = password;
        this.roles = roles;
        this.firstname = firstname;
        this.lastname = lastname;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserEntity user = (UserEntity) o;

        return userId != null && userId.equals(user.userId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
