CREATE SCHEMA IF NOT EXISTS study
    AUTHORIZATION postgres;

drop table if exists study.student_profile;
drop table if exists study.students;
drop table if exists study.courses;
drop table if exists study.trainers;

create table study.courses
(
    id   serial primary key,
    name varchar(128) NOT NULL unique
);

create table study.students
(
    id        serial primary key,
    name      varchar(128)                 NOT NULL,
    course_id int references study.courses NOT NULL
);

create table study.student_profile
(
    student_id    int primary key references study.students (id),
    score float NOT NULL
);

create table study.trainers
(
    id   serial primary key,
    name varchar(128)
);
