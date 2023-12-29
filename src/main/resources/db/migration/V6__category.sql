create table category (
    id_         bigserial                    not null,
    parent_ bigint references category(id_),
    name_ text,
    description_ text,
    created_ timestamp,
    updated_ timestamp default now(),
    primary key (id_)
);