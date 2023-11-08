package by.davlar.hibernate.dao;

import by.davlar.hibernate.entity.Seat;
import by.davlar.hibernate.entity.SeatPKey;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SeatDao extends AbstractDao<SeatPKey, Seat> {
    private static final SeatDao INSTANCE = new SeatDao();

    public static SeatDao getInstance() {
        return INSTANCE;
    }
}
