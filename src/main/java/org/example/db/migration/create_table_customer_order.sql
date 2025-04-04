
CREATE TABLE customer_order (
                                id VARCHAR PRIMARY KEY,
                                created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                status order_status_enum NOT NULL DEFAULT 'CREATED'
);