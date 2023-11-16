package by.davlar.hibernate.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
        name = "ticket",
        schema = "flight",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"flight_id", "seat_no"})}
)
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "passport_no")
    private String passportNo;
    @Column(name = "passenger_name")
    private String passengerName;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "flight_id")
    private Flight flight;
    @Column(name = "seat_no")
    private String seatNo;
    private BigDecimal cost;
}
