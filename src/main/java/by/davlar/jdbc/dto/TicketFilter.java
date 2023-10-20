package by.davlar.jdbc.dto;

public record TicketFilter(String passengerName,
                           String seatNo,
                           Integer limit,
                           Integer offset) {
}
