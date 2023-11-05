package by.davlar.entity;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class Flight {
    private Long id;
    private String flightNo;
    private LocalDateTime departureDate;
    private String departureAirportCode;
    private LocalDateTime arrivalDate;
    private String arrivalAirportCode;
    private Integer aircraftId;
    @Builder.Default
    private FlightStatus status = FlightStatus.SCHEDULED;
    private Airport departureAirport;
    private Airport arrivalAirport;
    private Aircraft aircraft;
}
