package com.epam.esm.repository;

import com.epam.esm.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    List<User> getAll();

    Optional<User> getById(long id);
}
