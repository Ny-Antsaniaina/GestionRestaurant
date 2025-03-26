CREATE TABLE ingredient_price_history (
                                          id varchar PRIMARY KEY,
                                          price float NOT NULL,
                                          date DATE DEFAULT current_date,
                                          ingredient_id varchar NOT NULL,
                                          FOREIGN KEY (ingredient_id) REFERENCES ingredient(id) on update cascade ON DELETE CASCADE
);