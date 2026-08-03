package io.github.George_Al3xander.dao.impl;

import io.github.George_Al3xander.dao.UserDao;
import io.github.George_Al3xander.model.User;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

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

        User user = entityManager
                .createQuery(qString, User.class)
                .setParameter("username", username)
                .getSingleResult();

        return Optional.ofNullable(user);
    }

    @Override
    public boolean existsByUsername(String username) {
        String qString = "SELECT COUNT(u) FROM User u WHERE u.username = :username";

        Long count = entityManager
                .createQuery(qString, Long.class)
                .setParameter("username", username.toLowerCase())
                .getSingleResult();

        return count > 0;
    }
}
