package by.davlar.hibernate.dao;

import by.davlar.hibernate.entity.Aircraft;
import by.davlar.hibernate.entity.Seat;
import by.davlar.hibernate.entity.SeatPKey;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AircraftDao extends AbstractDao<Integer, Aircraft> {
    private static final AircraftDao INSTANCE = new AircraftDao();

    public static AircraftDao getInstance() {
        return INSTANCE;
    }
}
