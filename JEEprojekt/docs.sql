create table docs
(
id integer not null generated always as identity (start with 1, increment by 1),
username varchar(20) not null,
description varchar(200) not null,
docname varchar(200) not null,
docfile blob not null,
datechanged date,
constraint docs_primary_key primary key (id)
);