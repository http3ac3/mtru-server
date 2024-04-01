-- liquibase formatted sql

-- changeset http3ac3:initial-tables dbms:postgresql

INSERT INTO role (name) VALUES ('ROLE_ADMIN');
INSERT INTO role (name) VALUES ('ROLE_LABHEAD');
INSERT INTO role (name) VALUES ('ROLE_USER');