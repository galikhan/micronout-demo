alter table product add primary key (id_);
create table product_size (
    id_         bigserial                    not null,
    product_ bigint references product(id_),
    article_ varchar(100),
    size_ varchar(100),
    created_ timestamp,
    updated_ timestamp default now(),
    primary key (id_)
);

