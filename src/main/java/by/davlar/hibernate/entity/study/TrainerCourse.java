package by.davlar.hibernate.entity.study;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "trainers_courses", schema = "study")
public class TrainerCourse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "trainer_id")
    private Trainer trainer;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "course_id")
    private Course course;

    public void setTrainer(Trainer trainer) {
        this.trainer = trainer;
        trainer.getTrainerCourse().add(this);
    }

    public void setCourse(Course course) {
        this.course = course;
        course.getTrainerCourse().add(this);
    }
}
