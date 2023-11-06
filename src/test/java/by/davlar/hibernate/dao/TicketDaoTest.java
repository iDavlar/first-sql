package by.davlar.hibernate.dao;

import by.davlar.hibernate.entity.Ticket;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class TicketDaoTest {

    private final TicketDao ticketDao = TicketDao.getInstance();

    @Test
    void save_noException() {
        try {
            Ticket ticket = Ticket.builder()
                    .passportNo("17gh14")
                    .passengerName("Michele")
                    .flightId(1L)
                    .seatNo("С15")
                    .cost(BigDecimal.valueOf(500))
                    .build();
            ticketDao.save(ticket);
            ticketDao.delete(ticket);
        } catch (Throwable e) {
            fail("Test get failed");
        }
    }

    @Test
    void delete_noException() {
        try {
            Ticket ticket = Ticket.builder()
                    .passportNo("17gh14")
                    .passengerName("Michele")
                    .flightId(1L)
                    .seatNo("С15")
                    .cost(BigDecimal.valueOf(500))
                    .build();
            ticketDao.save(ticket);
            ticketDao.delete(ticket);
        } catch (Throwable e) {
            fail("Test get failed");
        }
    }

    @Test
    void update() {
        try {

            var ticketOptional = ticketDao.findById(1L);
            assertTrue(ticketOptional.isPresent());

            var ticket = ticketOptional.get();
            var tempCoast = ticket.getCost();

            ticket.setCost(BigDecimal.valueOf(15));
            ticketDao.update(ticket);

            ticket.setCost(tempCoast);
            ticketDao.update(ticket);
        } catch (Throwable e) {
            fail("Test get failed");
        }
    }
}