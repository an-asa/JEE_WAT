create table records
(
record_id integer not null generated always as identity (start with 1, increment by 1),
id integer not null,
username varchar(20) not null,
daterecorded date,
constraint records_primary_key primary key (record_id),
constraint records_docs_fk foreign key (id) references docs(id) 
);