
CREATE DATABASE restaurant;

CREATE USER restaurant_user WITH ENCRYPTED PASSWORD ;


GRANT ALL PRIVILEGES ON DATABASE restaurant TO restaurant_user;


\c restaurant;


CREATE TYPE unit_enum AS ENUM ('G', 'L', 'U');


CREATE TABLE ingredient (
                            id SERIAL PRIMARY KEY,
                            name VARCHAR(255) NOT NULL UNIQUE,
                            last_modified TIMESTAMP,
                            unit_price DOUBLE PRECISION NOT NULL,
                            unit unit_enum NOT NULL
);


CREATE TABLE dish (
                      id SERIAL PRIMARY KEY,
                      name VARCHAR(255) NOT NULL UNIQUE,
                      sale_price DOUBLE PRECISION NOT NULL
);


CREATE TABLE dish_ingredient (
                                 dish_id INT NOT NULL,
                                 ingredient_id INT NOT NULL,
                                 quantity DOUBLE PRECISION NOT NULL,
                                 unit unit_enum NOT NULL,
                                 PRIMARY KEY (dish_id, ingredient_id),
                                 CONSTRAINT fk_dish FOREIGN KEY (dish_id) REFERENCES dish(id) ON DELETE CASCADE,
                                 CONSTRAINT fk_ingredient FOREIGN KEY (ingredient_id) REFERENCES ingredient(id) ON DELETE CASCADE
);


CREATE TABLE ingredient_price_history (
                                          id SERIAL PRIMARY KEY,
                                          ingredient_id INT NOT NULL,
                                          price DOUBLE PRECISION NOT NULL,
                                          date DATE NOT NULL,
                                          CONSTRAINT fk_ingredient_price FOREIGN KEY (ingredient_id) REFERENCES ingredient(id) ON DELETE CASCADE
);



INSERT INTO ingredient (name, last_modified, unit_price, unit) VALUES
                                                                   ('Saucisse', '2025-01-01 00:00', 20.0, 'G'),
                                                                   ('Huile', '2025-01-01 00:00', 10000.0, 'L'),
                                                                   ('Oeuf', '2025-01-01 00:00', 1000.0, 'U'),
                                                                   ('Pain', '2025-01-01 00:00', 1000.0, 'U');


INSERT INTO dish (name, sale_price) VALUES
    ('Hot Dog', 15000.0);


INSERT INTO dish_ingredient (dish_id, ingredient_id, quantity, unit) VALUES
                                                                         (1, 1, 100.0, 'G'), -- Saucisse
                                                                         (1, 2, 0.15, 'L'), -- Huile
                                                                         (1, 3, 1.0, 'U'), -- Oeuf
                                                                         (1, 4, 1.0, 'U'); -- Pain

INSERT INTO ingredient_price_history ( ingredient_id, price ,date) VALUES
                                                            (1, 20, '2025-01-01' ),
                                                            (2, 10000, '2025-01-01'),
                                                            ( 3,1000, '2025-01-01' ),
                                                            (4,1000, '2025-01-01');
