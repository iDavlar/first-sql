package by.davlar.jdbc.dao;

import by.davlar.jdbc.entity.Seat;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SeatDao extends AbstractDao<Seat, Seat> {

    private static final SeatDao INSTANCE = new SeatDao();

    private static final String SAVE_SQL = """
            INSERT INTO flight.seat
            (aircraft_id, seat_no) 
            VALUES (?, ?);
            """;

    private static final String DELETE_SQL = """
            DELETE
            FROM flight.seat
            WHERE aircraft_id = ?
              and seat_no = ?;
            """;

    private static final String FIND_ALL_SQL = """
            SELECT aircraft_id, seat_no
             FROM flight.seat
            """;

    private static final String FIND_BY_ID_SQL = """
            SELECT aircraft_id, seat_no
            FROM flight.seat
            WHERE aircraft_id = ?
              and seat_no = ?;
            """;

    private static final String UPDATE_SQL = """
            UPDATE flight.seat
            SET aircraft_id = ?,
              seat_no       = ?
            WHERE aircraft_id = ?
              and seat_no = ?;
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
    protected void setPrimaryKey(Seat entity, ResultSet keys) throws SQLException {
        entity.setAircraftId(keys.getInt("aircraft_id"));
        entity.setSeatNo(keys.getString("seat_no"));
    }

    @Override
    protected void setParametersSave(PreparedStatement statement, Seat entity) throws SQLException {
        statement.setInt(1, entity.getAircraftId());
        statement.setString(2, entity.getSeatNo());
    }

    @Override
    protected void setParametersUpdate(PreparedStatement statement, Seat entity) throws SQLException {
        statement.setInt(1, entity.getAircraftId());
        statement.setString(2, entity.getSeatNo());
        statement.setInt(3, entity.getAircraftId());
        statement.setString(4, entity.getSeatNo());
    }

    @Override
    protected Seat buildEntity(ResultSet resultSet) throws SQLException {
        return Seat.builder()
                .aircraftId(resultSet.getInt("aircraft_id"))
                .seatNo(resultSet.getString("seat_no"))
                .build();
    }

    @Override
    protected void setPrimaryKeyParameters(PreparedStatement statement, Seat key) throws SQLException {
        statement.setInt(1, key.getAircraftId());
        statement.setString(2, key.getSeatNo());
    }

    public static SeatDao getInstance() {
        return INSTANCE;
    }

    private SeatDao() {
    }
}
