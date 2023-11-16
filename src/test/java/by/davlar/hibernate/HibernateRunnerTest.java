package by.davlar.hibernate;

import by.davlar.hibernate.entity.study.Course;
import by.davlar.hibernate.entity.study.Student;
import by.davlar.hibernate.utils.ConfigurationManager;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HibernateRunnerTest {

    @Test
    void ManyToOne() {

        Course javaEnterprise = Course.builder()
                .name("Java Enterprise")
                .build();
        Course javaCore = Course.builder()
                .name("Java Core")
                .build();

        Student daniil = Student.builder()
                .name("Daniil")
                .build();
        Student semen = Student.builder()
                .name("Semen")
                .build();
        Student kiril = Student.builder()
                .name("Kiril")
                .build();
        Student petr = Student.builder()
                .name("Petr")
                .build();

        javaEnterprise.addStudent(daniil);
        javaEnterprise.addStudent(semen);
        javaEnterprise.addStudent(kiril);

        javaCore.addStudent(petr);

        List<Student> students = List.of(daniil, semen, kiril, petr);
        List<Course> courses = List.of(javaEnterprise, javaCore);

        Configuration configuration = ConfigurationManager.getConfiguration();
        try (var sessionFactory = configuration.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();

//            students.forEach(session::merge);
            courses.forEach(session::merge);

            Course course = session.get(Course.class, 1);
            course.getStudents().forEach(System.out::println);

            session.getTransaction().commit();
        }
    }

    @Test
    void OneToOne() {

        Course javaEnterprise = Course.builder()
                .name("Java Enterprise")
                .build();
        Course javaCore = Course.builder()
                .name("Java Core")
                .build();

        Student daniil = Student.builder()
                .name("Daniil")
                .build();
        Student semen = Student.builder()
                .name("Semen")
                .build();
        Student kiril = Student.builder()
                .name("Kiril")
                .build();
        Student petr = Student.builder()
                .name("Petr")
                .build();

        javaEnterprise.addStudent(daniil);
        javaEnterprise.addStudent(semen);
        javaEnterprise.addStudent(kiril);

        javaCore.addStudent(petr);

        List<Student> students = List.of(daniil, semen, kiril, petr);
        List<Course> courses = List.of(javaEnterprise, javaCore);

        Configuration configuration = ConfigurationManager.getConfiguration();
        try (var sessionFactory = configuration.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();

//            students.forEach(session::merge);
            courses.forEach(session::merge);

            Course course = session.get(Course.class, 1);
            course.getStudents().forEach(System.out::println);

            session.getTransaction().commit();
        }
    }
}