package by.davlar.hibernate.dao;

import by.davlar.hibernate.utils.ConfigurationManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Optional;

public abstract class AbstractDao<K, E>
        implements Dao<K, E> {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private Class<E> entityClass;

    private Class<E> getEntityClass() {
        log.trace("EntityClass request");
        if (entityClass == null) {
            entityClass = (Class<E>) ((ParameterizedType) getClass()
                    .getGenericSuperclass()).getActualTypeArguments()[1];
            log.trace("EntityClass lazy initialisation");
        }
        log.debug("entityClass: {}", entityClass);
        return entityClass;
    }

    @Override
    public E save(E entity) {
        log.debug("Start save method with entity = {}", entity);
        Configuration configuration = ConfigurationManager.getConfiguration();
        try (var sessionFactory = configuration.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            log.info("Start session {}", session);
            session.beginTransaction();
            session.merge(entity);
            session.getTransaction().commit();
            log.info("Commit session {}", session);
        } catch (Exception e) {
            log.error("Error during saving:", e);
            throw e;
        }

        log.trace("End save method and return {}", entity);
        return entity;
    }

    @Override
    public boolean delete(E entity) {
        log.debug("Start delete method with entity = {}", entity);
        Configuration configuration = ConfigurationManager.getConfiguration();
        try (var sessionFactory = configuration.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            log.info("Start session {}", session);
            session.beginTransaction();
            session.remove(entity);
            session.getTransaction().commit();
            log.info("Commit session {}", session);
            log.trace("End save method and return {}", true);
            return true;
        } catch (Exception e) {
            log.error("Error during deletion:", e);
            log.trace("End save method and return {}", false);
            return false;
        }
    }

    @Override
    public List<E> findAll() {
        log.debug("Start findAll method");
        Configuration configuration = ConfigurationManager.getConfiguration();
        try (var sessionFactory = configuration.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            log.info("Start session {}", session);
            session.beginTransaction();

            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<E> cq = cb.createQuery(getEntityClass());
            Root<E> rootEntry = cq.from(getEntityClass());
            CriteriaQuery<E> all = cq.select(rootEntry);

            TypedQuery<E> allQuery = session.createQuery(all);
            List<E> entities = allQuery.getResultList();

            session.getTransaction().commit();
            log.info("Commit session {}", session);
            log.trace("End findAll method and return {}", entities);
            return entities;
        } catch (Exception e) {
            log.error("Error when searching for all elements:", e);
            throw e;
        }
    }

    @Override
    public Optional<E> findById(K id) {
        log.debug("Start findById method with id = {}", id);
        Configuration configuration = ConfigurationManager.getConfiguration();
        try (var sessionFactory = configuration.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            log.info("Start session {}", session);
            session.beginTransaction();
            Optional<E> result = Optional.ofNullable(session.get(getEntityClass(), id));
            session.getTransaction().commit();
            log.info("Commit session {}", session);
            log.trace("End findById method and return {}", result);
            return result;
        } catch (Exception e) {
            log.error("Error when searching for element by id:", e);
            throw e;
        }
    }

    @Override
    public E update(E entity) {
        log.debug("Start update method with entity = {}", entity);
        Configuration configuration = ConfigurationManager.getConfiguration();
        try (var sessionFactory = configuration.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            log.info("Start session {}", session);
            session.beginTransaction();
            session.merge(entity);
            session.getTransaction().commit();
            log.info("Commit session {}", session);
        } catch (Exception e) {
            log.error("Error during update:", e);
            throw e;
        }
        log.trace("End update method and return {}", entity);
        return entity;
    }
}
