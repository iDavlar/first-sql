package by.davlar.jdbc.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Aircraft {
    private Integer id;
    private String model;
}
