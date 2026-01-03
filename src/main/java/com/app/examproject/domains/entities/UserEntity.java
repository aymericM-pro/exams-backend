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
    private Set<ExamEntity> exams = new HashSet<>();

    public UserEntity() {}

    public UserEntity(String email, String password, Set<Role> roles, String firstname, String lastname) {
        this.email = email;
        this.password = password;
        this.roles = roles;
        this.firstname = firstname;
        this.lastname = lastname;
    }
}
