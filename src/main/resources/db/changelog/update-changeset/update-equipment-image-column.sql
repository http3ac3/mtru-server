-- liquibase formatted sql

-- changeset http3ac3:update-image-column-datatype dbms:postgresql

ALTER TABLE equipment
ALTER COLUMN image_data
SET DATA TYPE TEXT;

ALTER TABLE equipment
ALTER COLUMN image_data
DROP NOT NULL;