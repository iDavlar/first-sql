/*
    1 Кто летел рейсом Минск (MNK) - Лондон (LDN) 2020-07-28 на месте B1

    2 Какие 2 перелета были самые длительные за все время

    3 Какая максимальная и минимальная продолжительность перелетов между Минском и Лондоном и сколько было всего таких перелетов

    4 Сколько мест осталось незанятыми 2020-06-14 на рейсе MN3002

    5 Какие имена встречаются чаще всего и какую долю от числа всех пассажиров они составляют

    6* Вывести имена пассажиров и сколько билетов пассажир купил за все время

    7* Вывести стоимость всех маршрутов по убыванию
        Отобразить разницу в стоимости между текущим и ближайшими маршрутами в отсортированном списке
 */

/* 1 */
select *
from ticket
where flight_id = (select f.id
                   from flight f
                   where f.departure_airport_code = (select code from airport where city = 'Минск')
                     and f.arrival_airport_code = (select code from airport where city = 'Лондон')
                     and date(f.departure_date) = '2020.07.28')
  and seat_no = 'B1';

/* 2 */
select f.*, age(f.arrival_date, f.departure_date) duration
from flight f
order by duration desc
limit 2;


/* 3 */
select count(*),
       max(age(f.arrival_date, f.departure_date)) max_duration,
       min(age(f.arrival_date, f.departure_date)) max_duration
from flight f
where f.departure_airport_code = (select code from airport where city = 'Минск')
  and f.arrival_airport_code = (select code from airport where city = 'Лондон');

/* 4 */
select f.flight_no,
       count(s.seat_no)                    total,
       count(t.seat_no)                    occupied,
       count(s.seat_no) - count(t.seat_no) free

from flight f
         join flight.seat s on f.aircraft_id = s.aircraft_id
         left join flight.ticket t on f.id = t.flight_id and s.seat_no = t.seat_no
where f.flight_no = 'MN3002'
  and date(f.departure_date) = '2020-06-14'
group by f.flight_no;

/* 5 */
select n.name,
       count(n.*),
       cast((cast(count(n.*) as dec(12, 2)) / (select count(*) from ticket) * 100) as int) procent
from (select substr(t.passenger_name, 0, position(' ' in t.passenger_name)) name
      from ticket t) n
group by n.name
order by count(n.*) desc;

/* 6 */
select t.passport_no, substr(t.passenger_name, 0, position(' ' in t.passenger_name)), count(*)
from ticket t
group by t.passport_no, substr(t.passenger_name, 0, position(' ' in t.passenger_name))
order by count(*) desc;

/* 7 */
select c1.arrival_airport_code,
       c1.departure_airport_code,
       c1.total_cost,
       c1.total_cost - c2.total_cost more_expensive,
       c3.total_cost - c1.total_cost cheaper

from (select f.arrival_airport_code,
             f.departure_airport_code,
             avg(nullif(t.cost, 0)) total_cost,
             row_number() over (order by avg(nullif(t.cost, 0)) desc)
      from flight f
               left join flight.ticket t on f.id = t.flight_id
      group by f.arrival_airport_code, f.departure_airport_code
      order by total_cost desc) c1
         left join (select f.arrival_airport_code,
                           f.departure_airport_code,
                           avg(nullif(t.cost, 0)) total_cost,
                           row_number() over (order by avg(nullif(t.cost, 0)) desc)
                    from flight f
                             left join flight.ticket t on f.id = t.flight_id
                    group by f.arrival_airport_code, f.departure_airport_code
                    order by total_cost desc) c2 on c2.row_number = c1.row_number + 1
         left join (select f.arrival_airport_code,
                           f.departure_airport_code,
                           avg(nullif(t.cost, 0)) total_cost,
                           row_number() over (order by avg(nullif(t.cost, 0)) desc)
                    from flight f
                             left join flight.ticket t on f.id = t.flight_id
                    group by f.arrival_airport_code, f.departure_airport_code
                    order by total_cost desc) c3 on c3.row_number = c1.row_number - 1;







