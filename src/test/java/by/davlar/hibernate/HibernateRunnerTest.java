package by.davlar.hibernate;

import by.davlar.hibernate.entity.study.*;
import by.davlar.hibernate.utils.ConfigurationManager;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HibernateRunnerTest {

    @BeforeEach
    public void createTestData() {

        Course javaEnterprise = Course.builder()
                .id(1)
                .name("Java Enterprise")
                .build();
        Course javaCore = Course.builder()
                .id(2)
                .name("Java Core")
                .build();

        Student daniil = Student.builder()
                .id(1)
                .name("Daniil")
                .build();
        Student semen = Student.builder()
                .id(2)
                .name("Semen")
                .build();
        Student kiril = Student.builder()
                .id(3)
                .name("Kiril")
                .build();
        Student petr = Student.builder()
                .id(4)
                .name("Petr")
                .build();

        StudentProfile studentProfile1 = StudentProfile.builder()
                .score(9.0F)
                .build();

        StudentProfile studentProfile2 = StudentProfile.builder()
                .score(5.0F)
                .build();

        StudentProfile studentProfile3 = StudentProfile.builder()
                .score(5.5F)
                .build();

        Trainer andrew = Trainer.builder()
                .id(1)
                .name("Andrew")
                .build();
        Trainer sasha = Trainer.builder()
                .id(2)
                .name("Sasha")
                .build();

        javaEnterprise.addStudent(daniil);
        javaEnterprise.addStudent(semen);
        javaEnterprise.addStudent(kiril);

        javaCore.addStudent(petr);

        List<Student> students = List.of(daniil, semen, kiril, petr);
        List<Course> courses = List.of(javaEnterprise, javaCore);
        List<StudentProfile> profiles = List.of(studentProfile1, studentProfile2, studentProfile3);
        List<Trainer> trainers = List.of(andrew, sasha);

        Configuration configuration = ConfigurationManager.getConfiguration();
        try (var sessionFactory = configuration.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();

            courses.forEach(session::merge);

            studentProfile1.setStudent(daniil);
            studentProfile2.setStudent(semen);
            studentProfile3.setStudent(kiril);

            profiles.forEach(session::merge);
            trainers.forEach(session::merge);

            session.getTransaction().commit();
        }
    }

    @Test
    void ManyToOne() {

        Configuration configuration = ConfigurationManager.getConfiguration();
        try (var sessionFactory = configuration.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();

            Course course = session.get(Course.class, 1);
            course.getStudents().forEach(System.out::println);

            session.getTransaction().commit();
        }
    }

    @Test
    void OneToOne() {

        Configuration configuration = ConfigurationManager.getConfiguration();
        try (var sessionFactory = configuration.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();

            Course course = session.get(Course.class, 1);
            course.getStudents().stream()
                    .filter(student -> student.getProfile().getScore() < 6.0F)
                    .forEach(session::remove);

            session.getTransaction().commit();
        }
    }

    @Test
    void courseDeletionTest() {

        Configuration configuration = ConfigurationManager.getConfiguration();
        try (var sessionFactory = configuration.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();

            Course javaEnterprise = session.get(Course.class, 1);
            javaEnterprise.getStudents().forEach(session::remove);

            session.remove(javaEnterprise);
            session.getTransaction().commit();
        }
    }

    @Test
    void ManyToMany() {

        Configuration configuration = ConfigurationManager.getConfiguration();
        try (var sessionFactory = configuration.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();

            Trainer trainer = session.get(Trainer.class, 1);
            Course course = session.get(Course.class, 1);

            TrainerCourse trainerCourse = new TrainerCourse();

            trainerCourse.setTrainer(trainer);
            trainerCourse.setCourse(course);

            session.merge(trainerCourse);
            session.getTransaction().commit();
        }
    }
}