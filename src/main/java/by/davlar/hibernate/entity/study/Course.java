package by.davlar.hibernate.entity.study;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"students", "trainerCourse"})
@Entity
@Table(name = "courses", schema = "study")
public class Course {
    @Id
    private Integer id;
    @Column(unique = true, nullable = false)
    private String name;

    @Builder.Default
    @OneToMany(mappedBy = "course", cascade = CascadeType.MERGE)
    private List<Student> students = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<TrainerCourse> trainerCourse = new ArrayList<>();

    public void addStudent(Student student) {
        students.add(student);
        student.setCourse(this);
    }
}
