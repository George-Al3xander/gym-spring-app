package io.github.George_Al3xander.dao.impl;

import io.github.George_Al3xander.dao.UserDao;
import io.github.George_Al3xander.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserDaoImpl implements UserDao {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public User save(User entity) {
        entityManager.persist(entity);
        return entity;
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(entityManager.find(User.class, id));

    }

    @Override
    public List<User> findAll() {
        return entityManager
                .createQuery("SELECT u FROM User u", User.class)
                .getResultList();
    }

    @Override
    public void delete(Long id) {
        User user = entityManager.getReference(User.class, id);
        entityManager.remove(user);
    }

    @Override
    public User update(User entity) {
        return entityManager.merge(entity);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        String qString = "SELECT u FROM User u WHERE u.username = :username";

        List<User> users = entityManager
                .createQuery(qString, User.class)
                .setParameter("username", username)
                .getResultList();

        return users.stream().findFirst();
    }

    @Override
    public long countByName(String firstName, String lastName) {
        String qString = "SELECT COUNT(u) FROM User u WHERE LOWER(u.firstName) = LOWER(:firstName) AND LOWER(u.lastName) = LOWER(:lastName)";

        return entityManager
                .createQuery(qString, Long.class)
                .setParameter("firstName", firstName)
                .setParameter("lastName", lastName)
                .getSingleResult();
    }
}
