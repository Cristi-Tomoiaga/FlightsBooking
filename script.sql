truncate table client restart identity cascade;
insert into client (username, name)
values ('cristi', 'Cristi'),
       ('bianca', 'Bianca'),
       ('alex', 'Alex'),
       ('mihai', 'Mihai'),
       ('maria', 'Maria');

truncate table flight restart identity cascade;
insert into flight ("from", "to", departure_time, landing_time, seats)
values ('Romania', 'USA', timestamp '2023-02-01 10:00', timestamp '2023-02-01 15:00', 50),
       ('Romania', 'Spain', timestamp '2023-02-10 13:00', timestamp '2023-02-10 15:00', 30),
       ('Bulgaria', 'Romania', timestamp '2023-01-02 20:00', timestamp '2023-01-02 21:00', 15),
       ('Romania', 'Italy', timestamp '2023-02-01 10:00', timestamp '2023-02-01 12:00', 20),
       ('Romania', 'Italy', timestamp '2023-02-01 13:00', timestamp '2023-02-01 14:00', 30),
       ('Romania', 'Italy', timestamp '2023-02-01 15:00', timestamp '2023-02-01 16:00', 50);

truncate table ticket restart identity cascade;



