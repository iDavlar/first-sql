package by.davlar.hibernate.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeatPKey implements Serializable {
    @Serial
    private static final long serialVersionUID = -7308597096247213308L;
    private Aircraft aircraft;
    private String seatNo;
}
