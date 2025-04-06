CREATE TABLE dish_order (
                            id varchar PRIMARY KEY,
                            order_id VARCHAR NOT NULL,
                            dish_id VARCHAR NOT NULL,
                            quantity INT CHECK (quantity > 0),
                            FOREIGN KEY (order_id) REFERENCES customer_order(id) ON DELETE CASCADE ON UPDATE CASCADE,
                            FOREIGN KEY (dish_id) REFERENCES dish(id) ON DELETE CASCADE ON UPDATE CASCADE
);