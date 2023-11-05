package by.davlar.dto;

public record TicketFilter(String passengerName,
                           String seatNo,
                           Integer limit,
                           Integer offset) {
}
