create table dictionary
(
    id_         bigserial not null,
    code_       varchar   not null,
    key_      varchar   not null,
    value_      varchar,
    created_ timestamp default now(),
    modified_ timestamp,
    primary key (id_),
    unique(code_,key_)
);