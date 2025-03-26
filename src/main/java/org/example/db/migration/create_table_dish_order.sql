CREATE TABLE dish_order (
                            id varchar PRIMARY KEY,
                            order_id VARCHAR NOT NULL,
                            dish_id VARCHAR NOT NULL,
                            quantity INT CHECK (quantity > 0),
                            status dish_order_status_enum DEFAULT 'CREATED' NOT NULL,
                            FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE ON UPDATE CASCADE,
                            FOREIGN KEY (dish_id) REFERENCES dish(id) ON DELETE CASCADE ON UPDATE CASCADE
);