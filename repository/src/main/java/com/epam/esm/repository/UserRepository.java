package com.epam.esm.repository;

import com.epam.esm.entity.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

/**
 * UserRepository provides functionality for interaction with storage,
 * which contains data about {@link User} entities
 */

@Repository
public class UserRepository {
    private static final String FIND_ALL = "SELECT u FROM User u";
    @PersistenceContext
    private EntityManager entityManager;


    /**
     * Searches user by specified id
     *
     * @param id - ID of the user to be find
     * @return User with specified id if user with specified id exists in the storage, e
     * else returns Optional.empty()
     */
    public Optional<User> getById(long id) {
        User user = entityManager.find(User.class, id);
        return Optional.ofNullable(user);
    }


    /**
     * Return a page of users within specified range
     *
     * @param size   -  maximal number of users in one page
     * @param offset - number of user from which page starts
     * @return List of all users located within specified range
     */
    public List<User> getPage(int size, int offset) {
        return entityManager
                .createQuery(FIND_ALL, User.class)
                .setFirstResult(offset)
                .setMaxResults(size)
                .getResultList();
    }

    public Optional<User> findByLogin(String login) {
        User optUser = entityManager.createQuery("select u from User u where u.login = ?1", User.class)
                .setParameter(1, login)
                .getSingleResult();
        return Optional.ofNullable(optUser);
    }

    public User createUser(User user) {
        entityManager.persist(user);
        entityManager.flush();
        return user;
    }
}
