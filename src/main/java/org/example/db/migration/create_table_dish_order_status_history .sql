CREATE TABLE dish_order_status_history (
                                           id Varchar PRIMARY KEY,
                                           dish_order_id varchar NOT NULL,
                                           status orderNOT NULL,
                                           changed_at TIMESTAMP,
                                           FOREIGN KEY (dish_order_id) REFERENCES dish_order(id) ON DELETE CASCADE ON UPDATE CASCADE
);