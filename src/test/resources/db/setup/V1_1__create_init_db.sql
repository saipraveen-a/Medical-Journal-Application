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
     references users (id);

INSERT INTO users(login_name, pwd, enabled, role, email) VALUES ('publisher1', '$2a$10$RC1.YFSL8o2vCIg6kB3fjuBB639BdjY6AD8rIw9oyWJLws/1Q6Lku', TRUE, 'PUBLISHER', 'publisher1@gmail.com');
INSERT INTO users(login_name, pwd, enabled, role, email) VALUES ('publisher2', '$2a$10$MUahUza86ErCxtsgpmMBDeR5VtoGHioRdl03/jQmkM/sk6L.Eg28e', TRUE, 'PUBLISHER', 'publisher2@gmail.com');
INSERT INTO users(login_name, pwd, enabled, role, email) VALUES ('user1', '$2a$10$WcgRF8VQ8DKt4h4Hz9pWv.6MXnIRmcPr0j9jqsseprsBwTD4w8WSm', TRUE, 'USER', 'user1@gmail.com');
INSERT INTO users(login_name, pwd, enabled, role, email) VALUES ('user2', '$2a$10$Q5bxyPXhHXFc1fRUMCRWR.GbgsXx9aGZdoEoEAz2JFEfckdyUKfOi', TRUE, 'USER', 'user2@gmail.com');

INSERT INTO publishers(user_id, name) VALUES (1, 'Test Publisher1');
INSERT INTO publishers(user_id, name) VALUES (2, 'Test Publisher 2');

INSERT INTO categories(id, name) VALUES (1, 'surgery');
INSERT INTO categories(id, name) VALUES (2, 'ophthalmology');
INSERT INTO categories(id, name) VALUES (3, 'therapy');
INSERT INTO categories(id, name) VALUES (4, 'stomatology');
INSERT INTO categories(id, name) VALUES (5, 'cardiology');


INSERT INTO journals(id, publisher_id, category_id, name, publish_date, uuid, notified) VALUES(1, 1, 3, 'Medicine', NOW(), '8305d848-88d2-4cbd-a33b-5c3dcc548056', FALSE);
INSERT INTO journals(id, publisher_id, category_id, name, publish_date, uuid, notified) VALUES(2, 1, 4, 'Test Journal', NOW(), '09628d25-ea42-490e-965d-cd4ffb6d4e9d', FALSE);
INSERT INTO journals(id, publisher_id, category_id, name, publish_date, uuid, notified) VALUES(3, 2, 5, 'Health', NOW(), '75f29692-237b-4116-95ed-645de5c57b4d', FALSE);

INSERT INTO subscriptions(id, user_id, category_id, date) VALUES(1, 3, 3, NOW());