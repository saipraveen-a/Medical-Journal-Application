create table categories (
    id bigint not null auto_increment,
    name varchar(255) not null,
    primary key (id)
);

create table journals (
    id bigint not null auto_increment,
    name varchar(255) not null,
    publish_date datetime not null,
    uuid varchar(255) not null,
    category_id bigint not null,
    publisher_id bigint not null,
    notified bit not null,
    primary key (id)
);

create table publishers (
    id bigint not null auto_increment,
    name varchar(255) not null,
    user_id bigint not null,
    primary key (id)
);

create table subscriptions (
    id bigint not null auto_increment,
    date datetime not null,
    category_id bigint not null,
    user_id bigint not null,
    primary key (id)
);

create table users (
    id bigint not null auto_increment,
    enabled bit not null,
    login_name varchar(255) not null,
    pwd varchar(255) not null,
    role varchar(255) not null,
    email varchar(255) not null,
    primary key (id)
);

alter table publishers
    add constraint UK_ml1xc0aovqkkm2p1lssgjkfas unique (user_id);

alter table journals
    add constraint FK_8yg6hsabxw2lgqjkbkij55qqx
    foreign key (category_id)
    references categories (id);

alter table journals
    add constraint FK_c7picib39dl7kxro2349cnpn9
    foreign key (publisher_id)
    references publishers (id);

alter table publishers
    add constraint FK_ml1xc0aovqkkm2p1lssgjkfas
    foreign key (user_id)
    references users (id);

alter table subscriptions
    add constraint FK_5n1jngces3c64v9dapehv1mae
    foreign key (category_id)
    references categories (id);

alter table subscriptions
     add constraint FK_tq3cq3gmsss8jjyb2l5sb1o6k
     foreign key (user_id)
     references users (id)
