package com.woowahan.techcamp.recipehub.user.repository;

import com.woowahan.techcamp.recipehub.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);
}
