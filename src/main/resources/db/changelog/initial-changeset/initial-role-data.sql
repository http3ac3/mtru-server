-- liquibase formatted sql

-- changeset http3ac3:initial-tables dbms:postgresql

INSERT INTO role (name) VALUES ('ROLE_ADMIN', 'ROLE_LABHEAD, ROLE_USER');