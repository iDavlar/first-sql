package by.davlar.hibernate.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "flight", schema = "flight")
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "flight_no")
    private String flightNo;
    @Column(name = "departure_date")
    private LocalDateTime departureDate;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "departure_airport_code")
    private Airport departureAirport;
    @Column(name = "arrival_date")
    private LocalDateTime arrivalDate;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "arrival_airport_code")
    private Airport arrivalAirport;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "aircraft_id")
    private Aircraft aircraftId;
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private FlightStatus status = FlightStatus.SCHEDULED;
}
