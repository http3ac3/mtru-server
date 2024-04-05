-- liquibase formatted sql

-- changeset http3ac3:rename-rent-datetime-columns dbms:postgresql

ALTER TABLE rent
RENAME COLUMN end_date TO end_datetime;

ALTER TABLE rent
RENAME COLUMN create_date TO create_datetime;