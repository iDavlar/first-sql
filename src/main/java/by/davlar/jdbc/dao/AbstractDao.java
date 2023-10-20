package by.davlar.jdbc.dao;

import by.davlar.jdbc.exception.DaoException;
import by.davlar.jdbc.utils.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class AbstractDao<K, E>
        implements Dao<K, E>, ConnectableDao<K, E> {

    protected abstract String getSaveSql();

    protected abstract String getDeleteSql();

    protected abstract String getFindAllSql();

    protected abstract String getFindDyIdSql();

    protected abstract String getUpdateSql();

    protected abstract void setPrimaryKey(E entity, ResultSet keys) throws SQLException;

    protected abstract void setParametersSave(PreparedStatement statement, E entity) throws SQLException;

    protected void setParametersDelete(PreparedStatement statement, K key) throws SQLException {
        setPrimaryKeyParameters(statement, key);
    };

    protected abstract void setParametersUpdate(PreparedStatement statement, E entity) throws SQLException;

    protected abstract E buildEntity(ResultSet resultSet) throws SQLException;

    protected void setPrimaryKeyParameters(PreparedStatement statement, K key) throws SQLException {
        statement.setObject(1, key);
    }

    @Override
    public E save(E entity) {
        try (var connection = ConnectionManager.get()) {
            return save(entity, connection);
        } catch (DaoException e) {
            throw e;
        } catch (Throwable e) {
            throw new DaoException(e);
        }
    }

    @Override
    public boolean delete(K key) {
        try (var connection = ConnectionManager.get()) {
            return delete(key, connection);
        } catch (DaoException e) {
            throw e;
        } catch (Throwable e) {
            throw new DaoException(e);
        }
    }

    @Override
    public List<E> findAll() {
        try (var connection = ConnectionManager.get()) {
            return findAll(connection);
        } catch (DaoException e) {
            throw e;
        } catch (Throwable e) {
            throw new DaoException(e);
        }
    }

    @Override
    public Optional<E> findById(K key) {
        try (var connection = ConnectionManager.get()) {
            return findById(key, connection);
        } catch (DaoException e) {
            throw e;
        } catch (Throwable e) {
            throw new DaoException(e);
        }
    }

    @Override
    public boolean update(E Entity) {
        try (var connection = ConnectionManager.get()) {
            return update(Entity, connection);
        } catch (DaoException e) {
            throw e;
        } catch (Throwable e) {
            throw new DaoException(e);
        }
    }

    @Override
    public E save(E Entity, Connection connection) {
        try (var statement = connection.prepareStatement(getSaveSql(), Statement.RETURN_GENERATED_KEYS)) {
            setParametersSave(statement, Entity);
            statement.executeUpdate();

            var keys = statement.getGeneratedKeys();
            if (keys.next()) {
                setPrimaryKey(Entity, keys);
            }
            return Entity;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public boolean delete(K key, Connection connection) {
        try (var statement = connection.prepareStatement(getDeleteSql())) {

            setParametersDelete(statement, key);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public List<E> findAll(Connection connection) {
        try (var statement = connection.prepareStatement(getFindAllSql())) {

            List<E> entities = new ArrayList<>();
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                entities.add(
                        buildEntity(resultSet)
                );
            }
            return entities;

        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public Optional<E> findById(K key, Connection connection) {
        try (var statement = connection.prepareStatement(getFindDyIdSql())) {
            setPrimaryKeyParameters(statement, key);

            ResultSet resultSet = statement.executeQuery();
            E entity = null;
            if (resultSet.next()) {
                entity = buildEntity(resultSet);
            }
            return Optional.ofNullable(entity);

        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public boolean update(E entity, Connection connection) {
        try (var statement = connection.prepareStatement(getUpdateSql())) {
            setParametersUpdate(statement, entity);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }
}
