CREATE TYPE movement AS ENUM ('ENTER','EXIT');

CREATE TABLE stock(
                      id varchar PRIMARY KEY,
                      movement movement,
                      quantity FLOAT,
                      unity unit_enum,
                      date TIMESTAMP,
                      id_ingredient varchar,
                      FOREIGN KEY (id_ingredient) REFERENCES ingredient(id)  on delete cascade on update cascade
);
