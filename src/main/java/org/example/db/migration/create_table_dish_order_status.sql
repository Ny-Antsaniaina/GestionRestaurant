CREATE TABLE dish_order_status (
                                   id SERIAL PRIMARY KEY,
                                   status dish_order_status_enum UNIQUE NOT NULL
);
