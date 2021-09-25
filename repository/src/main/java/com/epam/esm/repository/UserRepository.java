package com.epam.esm.repository;

import com.epam.esm.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * UserRepository provides functionality for interaction with storage,
 * which contains data about {@link User} entities
 */


public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByLogin(String login);
}
