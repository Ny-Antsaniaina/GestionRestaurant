INSERT INTO stock (id, movement, quantity, unity, date, id_ingredient) VALUES
                                                                           ('1', 'ENTER', 100, 'U', '2025-02-01 08:00:00.0', '3'),
                                                                           ('2', 'ENTER', 50, 'U', '2025-02-01 08:00:00.0', '4'),
                                                                           ('3', 'ENTER', 10000, 'G', '2025-02-01 08:00:00.0', '1'),
                                                                           ('4', 'ENTER', 20, 'L', '2025-02-01 08:00:00.0', '2');

INSERT INTO stock (id, movement, quantity, unity, date, id_ingredient) VALUES
                                                 select c.id , c.first_name , c.last_name , c.email , product ,price from "order" left join customer c on c.id = "order".customer_id                          ('5', 'EXIT', 10, 'U', '2025-02-02 10:00:00.0', '3'),
                                                                           ('6', 'EXIT', 10, 'U', '2025-02-03 15:00:00.0', '3'),
                                                                           ('7', 'EXIT', 20, 'U', '2025-02-05 16:00:00.0', '4');

INSERT INTO ingredient (id, name, unity) VALUES
                                             ('1', 'Saucisse', 'G'),
                                             ('2', 'Huile', 'L'),
                                             ('3', 'Oeuf', 'U'),
                                             ('4', 'Pain', 'U');

INSERT INTO ingredient(id,name,unity) VALUES
                                          ('5','sel', 'G'),
                                          ('6', 'riz', 'G')

INSERT INTO ingredient_price_history  (id, unit_price, date, id_ingredient) VALUES
                                                            ('5', 2.5,CURRENT_TIMESTAMP,'5'),
                                                            ('6', 3.5, CURRENT_TIMESTAMP,'6');

INSERT INTO dish (id, name, unit_price) VALUES
    ('1', 'Hot dog', 15000);

INSERT INTO dish_ingredient (dish_id, ingredient_id, quantity, unity) VALUES
                                                                                   ('1', '1', 100, 'G'),
                                                                                   ('1', '2', 0.15, 'L'),
                                                                                   ('1', '3', 1, 'U'),
                                                                                   ('1', '4', 1, 'U');
INSERT INTO ingredient_price_history  (id, price, date, ingredient_id) VALUES
                                                            ('1', 20, '2025-01-01', '1'),
                                                            ('2', 10000, '2025-01-01', '2'),
                                                            ('3', 1000, '2025-01-01', '3'),
                                                            ('4', 1000, '2025-01-01', '4');

INSERT INTO ingredient_price_history(id, price, date, ingredient_id) VALUES
                                                            ('5', 2.5,CURRENT_TIMESTAMP,'5'),
                                                            ('6', 3.5, CURRENT_TIMESTAMP,'6');

INSERT INTO stock (id, movement, quantity, unity, date, id_ingredient) VALUES
                                                                           ('1', 'ENTER', 100, 'U', '2025-02-01 08:00:00.0', '3'),
                                                                           ('2', 'ENTER', 50, 'U', '2025-02-01 08:00:00.0', '4'),
                                                                           ('3', 'ENTER', 10000, 'G', '2025-02-01 08:00:00.0', '1'),
                                                                           ('4', 'ENTER', 20, 'L', '2025-02-01 08:00:00.0', '2');