package com.joomart.lms.repository;

import com.joomart.lms.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Spring Data JPA can infer custom queries from method names, e.g.:
    // Optional<User> findByUsername(String username);
}
