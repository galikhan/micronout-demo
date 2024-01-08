alter table product add column created_ timestamp;
alter table product add column modified_ timestamp default now();