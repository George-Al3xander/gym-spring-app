package io.github.George_Al3xander.dao;

import java.util.List;
import java.util.Optional;

public interface CrudDao<T> {

    T save(T entity);

    Optional<T> findById(Long id);

    List<T> findAll();

    void delete(Long id);

    T update(T entity);
}
