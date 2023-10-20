package by.davlar.jdbc;

import by.davlar.jdbc.dao.*;
import by.davlar.jdbc.entity.*;
import by.davlar.jdbc.utils.ConnectionManager;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class JdbcRunner {
    public static void main(String[] args) {
        var airportDao = AirportDao.getInstance();
        var aircraftDao = AircraftDao.getInstance();
        var seatDao = SeatDao.getInstance();
        var ticketDao = TicketDao.getInstance();
        var flightDao = FlightDao.getInstance();
        try (var connection = ConnectionManager.get()) {
            Ticket ticket = ticketDao.findById(1L, connection).orElseThrow();

            Flight flight = flightDao.findById(ticket.getFlightId(), connection).orElseThrow();
            flight.setDepartureAirport(
                    airportDao.findById(flight.getDepartureAirportCode(), connection).orElseThrow()
            );
            flight.setArrivalAirport(
                    airportDao.findById(flight.getArrivalAirportCode(), connection).orElseThrow()
            );
            flight.setAircraft(
                    aircraftDao.findById(flight.getAircraftId(), connection).orElseThrow()
            );

            ticket.setFlight(flight);
            ticket.setSeat(
                    seatDao.findById(
                            new Seat(flight.getAircraftId(), ticket.getSeatNo(), flight.getAircraft()),
                            connection
                    ).orElseThrow()
            );
            System.out.println(ticket);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
