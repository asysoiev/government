create sequence citizen_seq
START WITH 10000
INCREMENT BY 1
;

create table citizen
(
    id        long         not null default citizen_seq.nextval,
    identifier  uuid  default random_uuid(),
    name      varchar(255) not null,
    surname   varchar(255) not null,
    birthday  date,
    gender    char         not null,
    death_date    date,
    comment   varchar(255),
    primary key (id),
    unique (identifier)
);
