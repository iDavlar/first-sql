package by.davlar.hibernate.entity.study;

import jakarta.persistence.*;
import lombok.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"student"})
@Entity
@Table(name = "student_profile", schema = "study")
public class StudentProfile {
    @Id
    @Column(name = "student_id")
    private Integer studentId;

    @OneToOne(cascade = CascadeType.MERGE)
    @PrimaryKeyJoinColumn
    private Student student;

    @Column(nullable = false)
    private Float score;

    public void setStudent(Student student) {
        this.student = student;
        student.setProfile(this);
        this.studentId = student.getId();
    }
}
