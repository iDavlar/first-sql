package by.davlar.jdbc.dao;

import by.davlar.entity.Aircraft;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AircraftDao extends AbstractDao<Integer, Aircraft> {

    private static final AircraftDao INSTANCE = new AircraftDao();

    private static final String SAVE_SQL = """
            INSERT INTO flight.aircraft
            (model) 
            VALUES (?);
            """;

    private static final String DELETE_SQL = """
            DELETE FROM flight.aircraft
            WHERE id = ?
            """;

    private static final String FIND_ALL_SQL = """
            SELECT id, model
             FROM flight.aircraft
            """;

    private static final String FIND_BY_ID_SQL = """
            SELECT id, model
             FROM flight.aircraft
             where id = ?
            """;

    private static final String UPDATE_SQL = """
            UPDATE flight.aircraft
            SET model = ?
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
    protected void setPrimaryKey(Aircraft entity, ResultSet keys) throws SQLException {
        entity.setId(keys.getInt("id"));
    }

    @Override
    protected void setParametersSave(PreparedStatement statement, Aircraft entity) throws SQLException {
        statement.setString(1, entity.getModel());
    }

    @Override
    protected void setParametersUpdate(PreparedStatement statement, Aircraft entity) throws SQLException {
        statement.setString(1, entity.getModel());
        statement.setInt(2, entity.getId());
    }

    @Override
    protected Aircraft buildEntity(ResultSet resultSet) throws SQLException {
        return Aircraft.builder()
                .id(resultSet.getInt("id"))
                .model(resultSet.getString("model"))
                .build();
    }

    public static AircraftDao getInstance() {
        return INSTANCE;
    }

    private AircraftDao() {
    }
}
