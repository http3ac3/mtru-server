-- liquibase formatted sql

-- changeset http3ac3:update-password-column-datatype dbms:postgresql

ALTER TABLE users
ALTER COLUMN password
SET DATA TYPE TEXT;