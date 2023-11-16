package by.davlar.hibernate.entity.study;


import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"profile"})
@Entity
@Table(name = "students", schema = "study")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private String name;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;
    @OneToOne(mappedBy = "student", cascade = CascadeType.ALL)
    private StudentProfile profile;
}
