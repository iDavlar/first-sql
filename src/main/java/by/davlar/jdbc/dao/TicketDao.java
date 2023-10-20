package by.davlar.jdbc.dao;

import by.davlar.jdbc.dto.TicketFilter;
import by.davlar.jdbc.entity.Ticket;
import by.davlar.jdbc.exception.DaoException;
import by.davlar.jdbc.utils.ConnectionManager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TicketDao
        implements Dao<Long, Ticket>, ConnectableDao<Long, Ticket> {
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

    private static final String FIND_ALL_SQL = """
            SELECT id, passport_no, passenger_name, flight_id, seat_no, cost
             FROM flight.ticket
            """;

    private static final String FIND_BY_ID_SQL = """
            SELECT id, passport_no, passenger_name, flight_id, seat_no, cost
             FROM flight.ticket
             where id = ?
            """;

    private static final String UPDATE_SQL = """
            UPDATE flight.ticket
            SET passport_no    = ?,
                passenger_name = ?,
                flight_id      = ?,
                seat_no        = ?,
                cost           = ?
            WHERE id = ?
            """;

    @Override
    public Ticket save(Ticket ticket) {
        try (var connection = ConnectionManager.get()) {
            return save(ticket, connection);
        } catch (DaoException e) {
            throw e;
        } catch (Throwable e) {
            throw new DaoException(e);
        }
    }

    @Override
    public boolean delete(Long id) {
        try (var connection = ConnectionManager.get()) {
            return delete(id, connection);
        } catch (DaoException e) {
            throw e;
        } catch (Throwable e) {
            throw new DaoException(e);
        }
    }

    @Override
    public List<Ticket> findAll() {
        try (var connection = ConnectionManager.get()) {
            return findAll(connection);
        } catch (DaoException e) {
            throw e;
        } catch (Throwable e) {
            throw new DaoException(e);
        }
    }

    public List<Ticket> findAll(TicketFilter filter) {
        List<Object> parameters = new ArrayList<>();
        List<String> whereSql = new ArrayList<>();
        if (filter.passengerName() != null) {
            parameters.add(filter.passengerName());
            whereSql.add("passenger_name = ?");
        }
        if (filter.seatNo() != null) {
            parameters.add("%" + filter.seatNo() + "%");
            whereSql.add("seat_no = ?");
        }
        parameters.add(filter.limit());
        parameters.add(filter.offset());
        String where = whereSql.stream().collect(Collectors.joining(
                " AND ",
                parameters.size() > 2 ? "\nWHERE " : " ",
                "\nLIMIT ?\nOFFSET ? "
        ));
        String sql = FIND_ALL_SQL + where;
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(sql)) {

            for (int i = 0; i < parameters.size(); i++) {
                statement.setObject(i + 1, parameters.get(i));
            }
            List<Ticket> tickets = new ArrayList<>();
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                tickets.add(
                        buildTicket(resultSet)
                );
            }
            return tickets;

        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public Optional<Ticket> findById(Long id) {
        try (var connection = ConnectionManager.get()) {
            return findById(id, connection);
        } catch (DaoException e) {
            throw e;
        } catch (Throwable e) {
            throw new DaoException(e);
        }
    }

    private static Ticket buildTicket(ResultSet resultSet) throws SQLException {
        return Ticket.builder()
                .id(resultSet.getLong("id"))
                .passportNo(resultSet.getString("passport_no"))
                .passengerName(resultSet.getString("passenger_name"))
                .flightId(resultSet.getLong("flight_id"))
                .seatNo(resultSet.getString("seat_no"))
                .cost(resultSet.getBigDecimal("cost"))
                .build();
    }

    @Override
    public boolean update(Ticket ticket) {
        try (var connection = ConnectionManager.get()) {
            return update(ticket, connection);
        } catch (DaoException e) {
            throw e;
        } catch (Throwable e) {
            throw new DaoException(e);
        }
    }

    public static TicketDao getInstance() {
        return INSTANCE;
    }

    private TicketDao() {
    }

    @Override
    public Ticket save(Ticket ticket, Connection connection) {
        try (var statement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, ticket.getPassportNo());
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

    @Override
    public boolean delete(Long id, Connection connection) {
        try (var statement = connection.prepareStatement(DELETE_SQL)) {

            statement.setLong(1, id);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public List<Ticket> findAll(Connection connection) {
        try (var statement = connection.prepareStatement(FIND_ALL_SQL)) {

            List<Ticket> tickets = new ArrayList<>();
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                tickets.add(
                        buildTicket(resultSet)
                );
            }
            return tickets;

        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public Optional<Ticket> findById(Long id, Connection connection) {
        try (var statement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            statement.setLong(1, id);

            ResultSet resultSet = statement.executeQuery();
            Ticket ticket = null;
            if (resultSet.next()) {
                ticket = buildTicket(resultSet);
            }
            return Optional.ofNullable(ticket);

        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public boolean update(Ticket ticket, Connection connection) {
        try (var statement = connection.prepareStatement(UPDATE_SQL)) {

            statement.setString(1, ticket.getPassportNo());
            statement.setString(2, ticket.getPassengerName());
            statement.setLong(3, ticket.getFlightId());
            statement.setString(4, ticket.getSeatNo());
            statement.setBigDecimal(5, ticket.getCost());
            statement.setLong(6, ticket.getId());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }
}
