package by.davlar.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class Seat {
    private Integer aircraftId;
    private String seatNo;
    private Aircraft aircraft;
}
