-- liquibase formatted sql

-- changeset http3ac3:initial-category-data dbms:postgresql

INSERT INTO category (name) VALUES
('Мебель'),
('Сетевое оборудование'),
('Научное оборудование'),
('ЭВМ'),
('Мультимедийное оборудование'),
('Доски'),
('Офисное оборудование'),
('Обучающие материалы'),
('Другое');