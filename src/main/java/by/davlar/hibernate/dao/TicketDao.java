package by.davlar.hibernate.dao;

import by.davlar.hibernate.entity.Ticket;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TicketDao extends AbstractDao<Long, Ticket> {

    private static final TicketDao INSTANCE = new TicketDao();

    public static TicketDao getInstance() {
        return INSTANCE;
    }
}
