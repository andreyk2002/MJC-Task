package com.epam.esm.repository;

import com.epam.esm.entity.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository {
    private static final String FIND_ALL = "SELECT u FROM User u";
    @PersistenceContext
    private EntityManager entityManager;


    public List<User> getAll() {
        return entityManager.createQuery(FIND_ALL, User.class).getResultList();
    }


    public Optional<User> getById(long id) {
        User user = entityManager.find(User.class, id);
        return Optional.ofNullable(user);
    }

    public List<User> getPage(int size, int offset) {
        return entityManager
                .createQuery(FIND_ALL, User.class)
                .setFirstResult(offset)
                .setMaxResults(size)
                .getResultList();
    }
}
