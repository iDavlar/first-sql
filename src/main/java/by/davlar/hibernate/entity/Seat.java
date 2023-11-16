package by.davlar.hibernate.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "seat", schema = "flight")
@IdClass(SeatPKey.class)
public class Seat {
    @ManyToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn(name = "aircraft_id")
    private Aircraft aircraft;
    @Id
    @Column(name = "seat_no")
    private String seatNo;
}
