package by.davlar.jdbc.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class Ticket {
    private Long id;
    private String passportNo;
    private String passengerName;
    private Long flightId;
    private String seatNo;
    private BigDecimal cost;
}
