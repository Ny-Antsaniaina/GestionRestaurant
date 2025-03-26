CREATE TABLE dish_ingredient (
                                 dish_id varchar NOT NULL,
                                 ingredient_id varchar NOT NULL,
                                 quantity float NOT NULL,
                                 unity unit_enum NOT NULL,
                                 PRIMARY KEY (dish_id, ingredient_id),
                                 FOREIGN KEY (dish_id) REFERENCES dish(id) on update cascade ON DELETE CASCADE,
                                 FOREIGN KEY (ingredient_id) REFERENCES ingredient(id) on update cascade ON DELETE CASCADE
);