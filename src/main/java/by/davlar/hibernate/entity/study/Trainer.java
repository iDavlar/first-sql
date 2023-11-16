package by.davlar.hibernate.entity.study;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"trainerCourse"})
@Entity
@Table(name = "trainers", schema = "study")
public class Trainer {
    @Id
    private Integer id;
    @Column(nullable = false)
    private String name;

    @Builder.Default
    @OneToMany(mappedBy = "trainer", cascade = CascadeType.ALL)
    private List<TrainerCourse> trainerCourse = new ArrayList<>();
}
