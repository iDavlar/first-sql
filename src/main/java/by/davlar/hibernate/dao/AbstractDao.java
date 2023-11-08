package by.davlar.hibernate.dao;

import by.davlar.hibernate.utils.ConfigurationManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.cfg.Configuration;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Optional;

public abstract class AbstractDao<K, E>
        implements Dao<K, E> {

    private Class<E> entityClass;

    private Class<E> getEntityClass() {
        if (entityClass == null) {
            entityClass = (Class<E>) ((ParameterizedType) getClass()
                    .getGenericSuperclass()).getActualTypeArguments()[1];
        }
        return entityClass;
    }

    @Override
    public E save(E entity) {
        Configuration configuration = ConfigurationManager.getConfiguration();
        try (var sessionFactory = configuration.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(entity);
            session.getTransaction().commit();
        }

        return entity;
    }

    @Override
    public boolean delete(E entity) {
        Configuration configuration = ConfigurationManager.getConfiguration();
        try (var sessionFactory = configuration.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(entity);
            session.getTransaction().commit();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<E> findAll() {
        Configuration configuration = ConfigurationManager.getConfiguration();
        try (var sessionFactory = configuration.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();

            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<E> cq = cb.createQuery(getEntityClass());
            Root<E> rootEntry = cq.from(getEntityClass());
            CriteriaQuery<E> all = cq.select(rootEntry);

            TypedQuery<E> allQuery = session.createQuery(all);
            List<E> entities = allQuery.getResultList();

            session.getTransaction().commit();

            return entities;
        }
    }

    @Override
    public Optional<E> findById(K id) {
        Configuration configuration = ConfigurationManager.getConfiguration();
        try (var sessionFactory = configuration.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();
            Optional<E> result = Optional.ofNullable(session.get(getEntityClass(), id));
            session.getTransaction().commit();

            return result;
        }
    }

    @Override
    public E update(E entity) {
        Configuration configuration = ConfigurationManager.getConfiguration();
        try (var sessionFactory = configuration.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(entity);
            session.getTransaction().commit();
        }
        return entity;
    }
}
