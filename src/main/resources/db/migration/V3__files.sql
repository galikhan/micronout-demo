create table file (
    id_         bigserial                    not null,
    container_  bigint                       not null,
    container_class_  varchar(50)            not null,
    uuid_ varchar(100),
    filename_ varchar(255),
    filepath_ text,
    created_ timestamp,
    updated_ timestamp default now(),
    primary key (id_)
);