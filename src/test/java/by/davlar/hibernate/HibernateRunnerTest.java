package by.davlar.hibernate;

import by.davlar.hibernate.dao.study.TrainerCourseDao;
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

        Course willDelete = Course.builder()
                .id(3)
                .name("Will delete")
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
        List<Course> courses = List.of(javaEnterprise, javaCore, willDelete);
        List<StudentProfile> profiles = List.of(studentProfile1, studentProfile2, studentProfile3);
        List<Trainer> trainers = List.of(andrew, sasha);

        List<TrainerCourse> trainerCourses = TrainerCourseDao.getInstance().findAll();

        Configuration configuration = ConfigurationManager.getConfiguration();
        try (var sessionFactory = configuration.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();

            trainerCourses.forEach(session::remove);

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

            session.persist(trainerCourse);
            session.getTransaction().commit();
        }
    }

    @Test
    void createNewTrainerWithCourses() {

        Trainer trainer = Trainer.builder()
                .id(3)
                .name("Test")
                .build();

        TrainerCourse trainerCourse1 = new TrainerCourse();
        TrainerCourse trainerCourse2 = new TrainerCourse();

        Configuration configuration = ConfigurationManager.getConfiguration();
        try (var sessionFactory = configuration.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();

            Course course1 = session.get(Course.class, 1);
            Course course2 = session.get(Course.class, 2);

            trainerCourse1.setTrainer(trainer);
            trainerCourse1.setCourse(course1);
            session.persist(trainerCourse1);

            trainerCourse2.setTrainer(trainer);
            trainerCourse2.setCourse(course2);
            session.persist(trainerCourse2);

            session.getTransaction().commit();
        }
    }

    @Test
    void changeCourse() {

        Configuration configuration = ConfigurationManager.getConfiguration();
        try (var sessionFactory = configuration.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();

            Course course = session.get(Course.class, 1);
            String tempName = course.getName();
            course.setName("Test");

            session.merge(course);

            course.setName(tempName);
            session.merge(course);

            session.getTransaction().commit();
        }
    }

    @Test
    void deleteCourse() {

        Configuration configuration = ConfigurationManager.getConfiguration();
        try (var sessionFactory = configuration.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();

            Course course = session.get(Course.class, 3);
            session.remove(course);

            assertNull(session.get(Course.class, 3));

            session.getTransaction().commit();
        }
    }
}