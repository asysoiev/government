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
    comment   varchar(255),
    primary key (id),
    unique (identifier)
);

insert into citizen(name, surname, birthday, gender)
values ('Dworkin', 'Barimen', DATEADD('YEAR', -1000, CURRENT_DATE), 'M');
insert into citizen(name, surname, birthday, gender)
values ('Oberon', 'Amber', DATEADD('YEAR', -500, CURRENT_DATE), 'M');
insert into citizen(name, surname, birthday, gender)
values ('Faiella', 'Amber', DATEADD('YEAR', -450, CURRENT_DATE), 'F');
insert into citizen(name, surname, birthday, gender)
values ('Corwin', 'Amber', DATEADD('YEAR', -350, CURRENT_DATE), 'M');
insert into citizen(name, surname, birthday, gender)
values ('Dara', 'Chaos', DATEADD('YEAR', -300, CURRENT_DATE), 'F');
insert into citizen(name, surname, birthday, gender)
values ('Merlin', 'Amber', DATEADD('YEAR', -200, CURRENT_DATE), 'M');