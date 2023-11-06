package by.davlar.hibernate.dao;

import by.davlar.hibernate.entity.Ticket;
import org.hibernate.cfg.Configuration;

import java.util.Optional;

public class TicketDao {

    private static final TicketDao INSTANCE = new TicketDao();

    public Ticket save(Ticket ticket) {
        Configuration configuration = new Configuration();
        configuration.configure();
        try (var sessionFactory = configuration.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(ticket);
            session.getTransaction().commit();
        }

        return ticket;
    }

    public boolean delete(Ticket ticket) {
        Configuration configuration = new Configuration();
        configuration.configure();
        try (var sessionFactory = configuration.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(ticket);
            session.getTransaction().commit();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Ticket update(Ticket ticket) {
        Configuration configuration = new Configuration();
        configuration.configure();
        try (var sessionFactory = configuration.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(ticket);
            session.getTransaction().commit();
        }
        return ticket;
    }

    public Optional<Ticket> findById(Long id) {
        Configuration configuration = new Configuration();
        configuration.configure();
        Ticket ticket = null;
        try (var sessionFactory = configuration.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();
            ticket = session.get(Ticket.class, id);
            session.getTransaction().commit();
        }

        return Optional.ofNullable(ticket);
    }

    public static TicketDao getInstance() {
        return INSTANCE;
    }
}
