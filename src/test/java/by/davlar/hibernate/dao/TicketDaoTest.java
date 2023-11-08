package by.davlar.hibernate.dao;

import by.davlar.hibernate.entity.Ticket;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class TicketDaoTest {

    private final TicketDao ticketDao = TicketDao.getInstance();
    private final FlightDao flightDao = FlightDao.getInstance();

    @Test
    void save_noFail() {
        try {
            Ticket ticket = Ticket.builder()
                    .passportNo("17gh14")
                    .passengerName("Michele")
                    .flight(flightDao.findById(1L).orElseThrow())
                    .seatNo("С15")
                    .cost(BigDecimal.valueOf(500))
                    .build();

            ticketDao.save(ticket);
            var id = ticket.getId();
            assertTrue(ticketDao.findById(id).isPresent());

            assertTrue(ticketDao.delete(ticket));
        } catch (NoSuchElementException e) {
            fail("FlightDao does not work correctly");
        } catch (Throwable e) {
            e.printStackTrace();
            fail("Test get failed");
        }
    }

    @Test
    void delete_noFail() {
        try {
            Ticket ticket = Ticket.builder()
                    .passportNo("17gh14")
                    .passengerName("Michele")
                    .flight(flightDao.findById(1L).orElseThrow())
                    .seatNo("С15")
                    .cost(BigDecimal.valueOf(500))
                    .build();

            ticketDao.save(ticket);
            var id = ticket.getId();
            assertTrue(ticketDao.findById(id).isPresent());

            assertTrue(ticketDao.delete(ticket));
        } catch (NoSuchElementException e) {
            fail("FlightDao does not work correctly");
        } catch (Throwable e) {
            e.printStackTrace();
            fail("Test get failed");
        }
    }

    @Test
    void update_noFail() {
        try {

            var ticketOptional = ticketDao.findById(1L);
            assertTrue(ticketOptional.isPresent());

            var ticket = ticketOptional.get();
            var tempCoast = ticket.getCost();

            ticket.setCost(BigDecimal.valueOf(15));
            ticketDao.update(ticket);
            assertNotEquals(ticket.getCost(), tempCoast);

            ticket.setCost(tempCoast);
            ticketDao.update(ticket);
            assertEquals(ticket.getCost(), tempCoast);
        } catch (Throwable e) {
            fail("Test get failed");
        }
    }
}