package by.davlar.jdbc.dao;

import by.davlar.entity.Airport;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AirportDao extends AbstractDao<String, Airport> {
    private static final AirportDao INSTANCE = new AirportDao();

    private static final String SAVE_SQL = """
            INSERT INTO flight.airport
            (code, country, city) 
            VALUES (?, ?, ?);
            """;

    private static final String DELETE_SQL = """
            DELETE FROM flight.airport
            WHERE code = ?
            """;

    private static final String FIND_ALL_SQL = """
            SELECT code, country, city
             FROM flight.airport
            """;

    private static final String FIND_BY_ID_SQL = """
            SELECT code, country, city
             FROM flight.airport
             where code = ?
            """;

    private static final String UPDATE_SQL = """
            UPDATE flight.airport
              SET country = ?,
                  city    = ?
              WHERE code = ?
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
    protected void setPrimaryKey(Airport entity, ResultSet keys) throws SQLException {
        entity.setCode(keys.getString("code"));
    }

    @Override
    protected void setParametersSave(PreparedStatement statement, Airport entity) throws SQLException {
        statement.setString(1, entity.getCode());
        statement.setString(2, entity.getCountry());
        statement.setString(3, entity.getCity());
    }

    @Override
    protected void setParametersUpdate(PreparedStatement statement, Airport entity) throws SQLException {
        statement.setString(1, entity.getCountry());
        statement.setString(2, entity.getCity());
        statement.setString(3, entity.getCode());
    }

    @Override
    protected Airport buildEntity(ResultSet resultSet) throws SQLException {
        return Airport.builder()
                .code(resultSet.getString("code"))
                .country(resultSet.getString("country"))
                .city(resultSet.getString("city"))
                .build();
    }

    public static AirportDao getInstance() {
        return INSTANCE;
    }

    private AirportDao() {
    }
}
