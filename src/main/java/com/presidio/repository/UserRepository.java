package com.presidio.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.presidio.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}
