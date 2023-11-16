package by.davlar.hibernate.dao;

import by.davlar.hibernate.entity.Aircraft;
import by.davlar.hibernate.entity.Airport;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AirportDao extends AbstractDao<String, Airport> {
    private static final AirportDao INSTANCE = new AirportDao();

    public static AirportDao getInstance() {
        return INSTANCE;
    }
}
