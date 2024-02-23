create table brand
(
    code_      varchar(20),
    name_      varchar,
    created_ timestamp default now(),
    modified_ timestamp,
    primary key (code_)
);
alter table product add column brand_ varchar(20) references  brand(code_);

insert into brand(code_, name_) values('santec', 'Santec');
insert into brand(code_, name_) values('valtec', 'Valtec');