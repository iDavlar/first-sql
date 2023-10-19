
CREATE SCHEMA IF NOT EXISTS flight
    AUTHORIZATION postgres;

create table flight.airport
(
    code    char(3) primary key,
    country varchar(128) NOT NULL,
    city    varchar(128) NOT NULL
);

create table flight.aircraft
(
    id    serial primary key,
    model varchar(128)
);

create table flight.seat
(
    aircraft_id int REFERENCES flight.aircraft,
    seat_no     char(4) NOT NULL,
    PRIMARY KEY (aircraft_id, seat_no)
);

create table flight.flight
(
    id                     bigserial primary key,
    flight_no              varchar(16)                       NOT NULL,
    departure_date         timestamp                         NOT NULL,
    departure_airport_code char(3) REFERENCES flight.airport (code) NOT NULL,
    arrival_date           timestamp                         NOT NULL,
    arrival_airport_code   char(3) REFERENCES flight.airport (code) NOT NULL,
    aircraft_id            int REFERENCES flight.aircraft           NOT NULL,
    status                 varchar(32)                       NOT NULL
);

create table flight.ticket
(
    id BIGSERIAL primary key ,
    passport_no varchar(64) NOT NULL ,
    passenger_name varchar(256) NOT NULL ,
    flight_id bigint REFERENCES flight.flight NOT NULL,
    seat_no char(4) NOT NULL,
    cost NUMERIC(8, 2) NOT NULL ,
    UNIQUE (flight_id, seat_no)
);




