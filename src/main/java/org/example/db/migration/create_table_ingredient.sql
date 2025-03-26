
CREATE DATABASE restaurant;

CREATE USER restaurant_user WITH ENCRYPTED PASSWORD ;


GRANT ALL PRIVILEGES ON DATABASE restaurant TO restaurant_user;


\c restaurant;


CREATE TYPE unit_enum AS ENUM ('G', 'L', 'U');


CREATE TABLE ingredient (
                            id VARCHAR PRIMARY KEY,
                            name VARCHAR(255) NOT NULL ,
                            unity unit_enum
);













