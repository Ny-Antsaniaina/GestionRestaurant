
CREATE TYPE order_status_enum AS ENUM ('CREATED', 'CONFIRMED', 'IN_PREPARATION', 'COMPLETED', 'SERVED');
CREATE TYPE dish_order_status_enum AS ENUM ('CREATED', 'CONFIRMED', 'IN_PREPARATION', 'COMPLETED', 'SERVED');

CREATE TABLE order_status (
                              id SERIAL PRIMARY KEY,
                              status order_status_enum UNIQUE NOT NULL
);
