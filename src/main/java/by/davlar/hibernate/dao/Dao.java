package by.davlar.hibernate.dao;

import java.util.List;
import java.util.Optional;

public interface Dao<K, E> {
    E save(E entity);

    boolean delete(E entity);

    List<E> findAll();

    Optional<E> findById(K id);

    E update(E entity);
}
