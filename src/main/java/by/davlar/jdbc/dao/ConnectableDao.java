package by.davlar.jdbc.dao;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public interface ConnectableDao<K, E> {
    E save(E e, Connection connection);

    boolean delete(K id, Connection connection);

    List<E> findAll(Connection connection);

    Optional<E> findById(K id, Connection connection);

    boolean update(E e, Connection connection);
}
