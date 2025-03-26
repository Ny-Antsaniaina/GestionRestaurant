CREATE TABLE order (
                        id VARCHAR PRIMARY KEY,
                        created_at TIMESTAMP ,
                        status order_status_enum DEFAULT 'CREATED' NOT NULL
);

