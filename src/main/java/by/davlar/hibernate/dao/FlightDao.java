package by.davlar.hibernate.dao;

import by.davlar.hibernate.entity.Airport;
import by.davlar.hibernate.entity.Flight;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FlightDao extends AbstractDao<Long, Flight> {
    private static final FlightDao INSTANCE = new FlightDao();

    public static FlightDao getInstance() {
        return INSTANCE;
    }
}
