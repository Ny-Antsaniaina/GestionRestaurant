
CREATE TYPE order_status_enum AS ENUM ('CREATED', 'CONFIRMED', 'IN_PREPARATION', 'COMPLETED', 'SERVED');

create table orders (
    id varchar primary key ,
    satus order_status_enum default 'CREATED',
    create_at TIMESTAMP default NOW()
)
