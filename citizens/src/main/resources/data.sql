create table citizen
(
    id        long         not null,
    uuid      varchar(36)  not null,
    name      varchar(255) not null,
    surname   varchar(255) not null,
    parent_id long,
    birthday  datetime,
    gender    char         not null,
    comment   varchar(255),
    primary key (id),
    unique (uuid)
);