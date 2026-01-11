package com.app.examproject.repositories;

import com.app.examproject.domains.entities.Role;
import com.app.examproject.domains.entities.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    @Query("""
select u from UserEntity u
where lower(u.firstname) like lower(concat('%', coalesce(:firstname, ''), '%'))
  and (:role is null or :role member of u.roles)
""")
    Page<UserEntity> search(
            @Param("role") Role role,
            @Param("firstname") String firstname,
            Pageable pageable
    );
}
