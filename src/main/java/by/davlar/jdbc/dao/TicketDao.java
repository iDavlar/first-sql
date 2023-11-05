package by.davlar.jdbc.dao;

import by.davlar.dto.TicketFilter;
import by.davlar.entity.Ticket;
import by.davlar.exception.DaoException;
import by.davlar.utils.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TicketDao
        extends AbstractDao<Long, Ticket> {
    private static final TicketDao INSTANCE = new TicketDao();

    private static final String SAVE_SQL = """
            INSERT INTO flight.ticket
            (passport_no, passenger_name, flight_id, seat_no, cost) 
            VALUES (?, ?, ?, ?, ?);
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
    protected String getSaveSql() {
        return SAVE_SQL;
    }

    @Override
    protected String getDeleteSql() {
        return DELETE_SQL;
    }

    @Override
    protected String getFindAllSql() {
        return FIND_ALL_SQL;
    }

    @Override
    protected String getFindDyIdSql() {
        return FIND_BY_ID_SQL;
    }

    @Override
    protected String getUpdateSql() {
        return UPDATE_SQL;
    }

    @Override
    protected void setPrimaryKey(Ticket entity, ResultSet keys) throws SQLException {
        entity.setId(keys.getLong("id"));
    }

    @Override
    protected void setParametersSave(PreparedStatement statement, Ticket entity) throws SQLException {
        setNoPrimaryParameters(statement, entity);
    }

    @Override
    protected void setParametersUpdate(PreparedStatement statement, Ticket entity) throws SQLException {
        setNoPrimaryParameters(statement, entity);
        statement.setLong(6, entity.getId());
    }

    private void setNoPrimaryParameters(PreparedStatement statement, Ticket entity) throws SQLException {
        statement.setString(1, entity.getPassportNo());
        statement.setString(2, entity.getPassengerName());
        statement.setLong(3, entity.getFlightId());
        statement.setString(4, entity.getSeatNo());
        statement.setBigDecimal(5, entity.getCost());
    }

    @Override
    protected Ticket buildEntity(ResultSet resultSet) throws SQLException {
        return Ticket.builder()
                .id(resultSet.getLong("id"))
                .passportNo(resultSet.getString("passport_no"))
                .passengerName(resultSet.getString("passenger_name"))
                .flightId(resultSet.getLong("flight_id"))
                .seatNo(resultSet.getString("seat_no"))
                .cost(resultSet.getBigDecimal("cost"))
                .build();
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
        String sql = getFindAllSql() + where;
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(sql)) {

            for (int i = 0; i < parameters.size(); i++) {
                statement.setObject(i + 1, parameters.get(i));
            }
            List<Ticket> tickets = new ArrayList<>();
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                tickets.add(
                        buildEntity(resultSet)
                );
            }
            return tickets;

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
