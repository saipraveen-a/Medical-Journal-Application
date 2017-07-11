INSERT INTO users(login_name, pwd, enabled, role, email) VALUES ('publisher1', '$2a$10$RC1.YFSL8o2vCIg6kB3fjuBB639BdjY6AD8rIw9oyWJLws/1Q6Lku', TRUE, 'PUBLISHER', 'publisher1@gmail.com');
INSERT INTO users(login_name, pwd, enabled, role, email) VALUES ('publisher2', '$2a$10$MUahUza86ErCxtsgpmMBDeR5VtoGHioRdl03/jQmkM/sk6L.Eg28e', TRUE, 'PUBLISHER', 'publisher2@gmail.com');
INSERT INTO users(login_name, pwd, enabled, role, email) VALUES ('user1', '$2a$10$WcgRF8VQ8DKt4h4Hz9pWv.6MXnIRmcPr0j9jqsseprsBwTD4w8WSm', TRUE, 'USER', 'user1@gmail.com');
INSERT INTO users(login_name, pwd, enabled, role, email) VALUES ('user2', '$2a$10$Q5bxyPXhHXFc1fRUMCRWR.GbgsXx9aGZdoEoEAz2JFEfckdyUKfOi', TRUE, 'USER', 'user2@gmail.com');

INSERT INTO publishers(user_id, name) VALUES (1, 'Test Publisher1 ');
INSERT INTO publishers(user_id, name) VALUES (2, 'Test Publisher 2');

INSERT INTO categories(id, name) VALUES (1, 'immunology');
INSERT INTO categories(id, name) VALUES (2, 'pathology');
INSERT INTO categories(id, name) VALUES (3, 'endocrinology');
INSERT INTO categories(id, name) VALUES (4, 'microbiology');
INSERT INTO categories(id, name) VALUES (5, 'neurology');


INSERT INTO journals(id, publisher_id, category_id, name, publish_date, uuid, notified) VALUES(1, 1, 3, 'Medicine', NOW(), '8305d848-88d2-4cbd-a33b-5c3dcc548056', FALSE);
INSERT INTO journals(id, publisher_id, category_id, name, publish_date, uuid, notified) VALUES(2, 1, 4, 'Test Journal', NOW(), '09628d25-ea42-490e-965d-cd4ffb6d4e9d', FALSE);
INSERT INTO journals(id, publisher_id, category_id, name, publish_date, uuid, notified) VALUES(3, 2, 5, 'Health', NOW(), '75f29692-237b-4116-95ed-645de5c57b4d', FALSE);

INSERT INTO subscriptions(id, user_id, category_id, date) VALUES(1, 3, 3, NOW());