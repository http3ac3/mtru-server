-- liquibase formatted sql

-- changeset http3ac3:add-initial-cost-column-equipment dbms:postgresql
ALTER TABLE equipment
ADD COLUMN initial_cost DECIMAL(10, 2);

UPDATE equipment SET initial_cost = 0.00;

ALTER TABLE equipment
ALTER COLUMN initial_cost SET NOT NULL;