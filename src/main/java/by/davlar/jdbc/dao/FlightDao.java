package by.davlar.jdbc.dao;

import by.davlar.jdbc.entity.Flight;
import by.davlar.jdbc.entity.FlightStatus;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class FlightDao extends AbstractDao<Long, Flight> {
    private static final FlightDao INSTANCE = new FlightDao();

    private static final String SAVE_SQL = """
            INSERT INTO flight.flight
            (flight_no, departure_date, departure_airport_code, arrival_date, arrival_airport_code, aircraft_id, status) 
            VALUES (?, ?, ?, ?, ?, ?, ?);
            """;

    private static final String DELETE_SQL = """
            DELETE
            FROM flight.flight
            WHERE id = ?;
            """;

    private static final String FIND_ALL_SQL = """
            SELECT flight_no, departure_date, departure_airport_code, arrival_date, 
                    arrival_airport_code, aircraft_id, status
             FROM flight.flight;
            """;

    private static final String FIND_BY_ID_SQL = """
            SELECT id, flight_no, departure_date, departure_airport_code, arrival_date, 
                    arrival_airport_code, aircraft_id, status
            FROM flight.flight
            WHERE id = ?;
            """;

    private static final String UPDATE_SQL = """
            UPDATE flight.flight
            SET flight_no           = ?,
             departure_date         = ?,
             departure_airport_code = ?,
             arrival_date           = ?,
             arrival_airport_code   = ?,
             aircraft_id            = ?,
             status                 = ?
            WHERE id = ?;
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
    protected void setPrimaryKey(Flight entity, ResultSet keys) throws SQLException {
        entity.setId(keys.getLong("id"));
    }

    @Override
    protected void setParametersSave(PreparedStatement statement, Flight entity) throws SQLException {
        statement.setString(1, entity.getFlightNo());
        statement.setTimestamp(2, Timestamp.valueOf(entity.getDepartureDate()));
        statement.setString(3, entity.getDepartureAirportCode());
        statement.setTimestamp(4, Timestamp.valueOf(entity.getArrivalDate()));
        statement.setString(5, entity.getArrivalAirportCode());
        statement.setInt(6, entity.getAircraftId());
        statement.setString(7, entity.getStatus().toString());
    }

    @Override
    protected void setParametersUpdate(PreparedStatement statement, Flight entity) throws SQLException {
        setParametersSave(statement, entity);
        statement.setLong(8, entity.getId());
    }

    @Override
    protected Flight buildEntity(ResultSet resultSet) throws SQLException {
        return Flight.builder()
                .id(resultSet.getLong("id"))
                .flightNo(resultSet.getString("flight_no"))
                .departureDate(resultSet.getTimestamp("departure_date").toLocalDateTime())
                .departureAirportCode(resultSet.getString("departure_airport_code"))
                .arrivalDate(resultSet.getTimestamp("arrival_date").toLocalDateTime())
                .arrivalAirportCode(resultSet.getString("arrival_airport_code"))
                .aircraftId(resultSet.getInt("aircraft_id"))
                .status(FlightStatus.valueOf(resultSet.getString("status")))
                .build();
    }

    public static FlightDao getInstance() {
        return INSTANCE;
    }

    private FlightDao() {
    }
}
