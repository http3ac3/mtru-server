-- liquibase formatted sql

-- changeset http3ac3:initial-category-data dbms:postgresql

INSERT INTO subcategory (name, category_id)  VALUES
('Парты', 1),
('Столы', 1),
('Стулья ученические', 1),
('Стулья компьютерные', 1),
('Шкафы', 1),
('Рабочие места студентов', 1);


INSERT INTO subcategory (name, category_id)  VALUES
('Коммутаторы', 2),
('Маршрутизаторы', 2),
('Сетевые кабели и коммуникация', 2),
('Wi-Fi роутеры', 2);


INSERT INTO subcategory (name, category_id)  VALUES
('Микроскопы', 3),
('Лазерные установки', 3),
('Линзы', 3),
('Осциллографы', 3),
('Зеркала', 3),
('Призмы', 3),
('Объективы', 3);

INSERT INTO subcategory (name, category_id)  VALUES
('Персональные компьютеры', 4),
('Мини-компьютеры', 4),
('Ноутбуки', 4),
('Модули ЭВМ', 4);

INSERT INTO subcategory (name, category_id)  VALUES
('Проекторы', 5),
('Телевизоры', 5);

INSERT INTO subcategory (name, category_id)  VALUES
('Доски одно-элементные', 6),
('Доски дву-элементные', 6),
('Доски трех-элементные', 6),
('Доски интерактивные', 6);

INSERT INTO subcategory (name, category_id)  VALUES
('МФУ', 7),
('Принтеры', 7),
('Сканеры', 7),
('Факсы', 7);

INSERT INTO subcategory (name, category_id)  VALUES
('Информационные стенды', 8),
('Лабораторные стенды', 8);

INSERT INTO subcategory (name, category_id)  VALUES
('Другое', 9);



