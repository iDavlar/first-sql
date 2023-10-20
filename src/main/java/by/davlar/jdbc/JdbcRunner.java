package by.davlar.jdbc;

import by.davlar.jdbc.dao.TicketDao;
import by.davlar.jdbc.entity.Ticket;
import by.davlar.jdbc.utils.ConnectionManager;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcRunner {
    public static void main(String[] args) throws SQLException {
        var ticketDao = TicketDao.getInstance();

        Ticket ticket = Ticket.builder()
                .passportNo("ght123")
                .passengerName("Daniil")
                .flightId(4L)
                .seatNo("5B")
                .cost(BigDecimal.valueOf(400L))
                .build();

        ticketDao.save(ticket);
        System.out.println(ticket);
        System.out.println(ticketDao.delete(ticket.getId()));
        ticketDao.findAll().forEach(System.out::println);

    }

    private static void updateWithTransaction() {
        try (var connection = ConnectionManager.get()) {
            connection.setAutoCommit(false);

            try {
                updateCostInTicketByFlightId(connection, 1L, BigDecimal.valueOf(314L));
                updateStatusInFlightById(connection, 1L, "DONT CARE");
                executeCorruptSQL(connection);

                connection.commit();
                System.out.println("Commit!");
            } catch (Exception e) {
                connection.rollback();
                System.out.println("Rollback!");
            }
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void executeCorruptSQL(Connection connection) {
        String sql = """
                select flight_id1234
                from flight.flight
                """;
        try (var statement = connection.prepareStatement(sql)) {
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean updateStatusInFlightById(Connection connection, long id, String status) {
        String sql = """
                update flight.flight
                set status = ?
                where id = ?;
                """;
        try (var statement = connection.prepareStatement(sql)) {
            statement.setString(1, status);
            statement.setLong(2, id);
            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean updateCostInTicketByFlightId(Connection connection, long fligthId, BigDecimal newCost) {
        String sql = """
                update flight.ticket
                set cost = ?
                where flight_id = ?;
                """;
        try (var statement = connection.prepareStatement(sql)) {
            statement.setBigDecimal(1, newCost);
            statement.setLong(2, fligthId);
            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean updatePassportInTicketById(long id, String newPassport) {
        String sql = """
                update flight.ticket
                set passport_no = ?
                where id = ?;
                """;
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(sql)) {
            statement.setString(1, newPassport);
            statement.setLong(2, id);
            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void showPassengerPurchases() {
        String sql = """
                select t.passport_no, 
                substr(t.passenger_name, 0, position(' ' in t.passenger_name)) name, 
                count(*) count
                                         from flight.ticket t
                                         group by t.passport_no, substr(t.passenger_name, 0, position(' ' in t.passenger_name))
                                         order by count(*) desc;
                """;
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(sql)) {
            statement.setMaxRows(6);
            ResultSet resultSet = statement.executeQuery();

            StringBuilder sb = new StringBuilder();
            sb.append("Passenger purchases:\n");
            while (resultSet.next()) {
                sb.append("Name: %10s count %3d%n"
                        .formatted(resultSet.getString("name"),
                                resultSet.getInt("count"))
                );
            }
            System.out.println(sb);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void showMostCommonPassengerNames() {
        String sql = """
                select n.name,
                       count(n.*) count
                from (select substr(t.passenger_name, 0, position(' ' in t.passenger_name)) name
                      from flight.ticket t) n
                group by n.name
                order by count(n.*) desc;
                """;
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(sql)) {
            statement.setMaxRows(6);
            ResultSet resultSet = statement.executeQuery();

            StringBuilder sb = new StringBuilder();
            sb.append("The most common passenger names:\n");
            while (resultSet.next()) {
                sb.append("Name: %10s count %3d%n"
                        .formatted(resultSet.getString("name"),
                                resultSet.getInt("count"))
                );
            }
            System.out.println(sb);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
