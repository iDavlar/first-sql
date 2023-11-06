package by.davlar.hibernate;


import by.davlar.hibernate.entity.Ticket;
import org.hibernate.cfg.Configuration;

import java.math.BigDecimal;

public class HibernateRunner {
    public static void main(String[] args) {
        Configuration configuration = new Configuration();
        configuration.configure();
        try (var sessionFactory = configuration.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();


//            session.save(Ticket.builder()
//                    .id(1L)
//                    .passportNo("17gh14")
//                    .passengerName("Michele")
//                    .flightId(1L)
//                    .seatNo("С15")
//                    .cost(BigDecimal.valueOf(500))
//                    .build()
//            );
            Ticket ticket = session.get(Ticket.class, 1L);
            System.out.println(ticket);
            session.getTransaction().commit();
        }
    }
}
