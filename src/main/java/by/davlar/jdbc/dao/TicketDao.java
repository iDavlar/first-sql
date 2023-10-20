package by.davlar.jdbc.dao;

import by.davlar.jdbc.entity.Ticket;
import by.davlar.jdbc.exeption.DaoException;
import by.davlar.jdbc.utils.ConnectionManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TicketDao {
    private static final TicketDao INSTANCE = new TicketDao();

    private static final String SAVE_SQL = """
            INSERT INTO flight.ticket
            (passport_no, passenger_name, flight_id, seat_no, cost) 
            VALUES (?,?,?,?,?);
            """;

    private static final String DELETE_SQL = """
            DELETE FROM flight.ticket
            WHERE id = ?
            """;

    public Ticket save(Ticket ticket) {
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {

            statement.setLong(1, ticket.getId());
            statement.setString(2, ticket.getPassengerName());
            statement.setLong(3, ticket.getFlightId());
            statement.setString(4, ticket.getSeatNo());
            statement.setBigDecimal(5, ticket.getCost());

            statement.executeUpdate();

            var keys = statement.getGeneratedKeys();
            if (keys.next()) {
                ticket.setId(keys.getLong("id"));
            }
            return ticket;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public boolean delete(Long id) {
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(DELETE_SQL)) {

            statement.setLong(1, id);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public static TicketDao getInstance() {
        return INSTANCE;
    }

    private TicketDao() {
    }
}
