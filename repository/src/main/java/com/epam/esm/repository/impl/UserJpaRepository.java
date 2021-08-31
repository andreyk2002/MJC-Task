package com.epam.esm.repository.impl;

import com.epam.esm.entity.User;
import com.epam.esm.repository.UserRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class UserJpaRepository implements UserRepository {
    private static final String FIND_ALL = "SELECT u FROM User u";
    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public List<User> getAll() {
        return entityManager.createQuery(FIND_ALL).getResultList();
    }

    @Override
    public Optional<User> getById(long id) {
        User user = entityManager.find(User.class, id);
        return Optional.ofNullable(user);
    }
}
