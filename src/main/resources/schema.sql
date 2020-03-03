drop table if exists people;

create table people (
    id bigint auto_increment primary key,
    name varchar(50) not null
);

insert into people (name) values ('Adam'), ('Eden');