CREATE TABLE order_status_history (
                                      id SERIAL PRIMARY KEY,
                                      order_id VARCHAR NOT NULL,
                                      status order_status_enum NOT NULL,
                                      changed_at TIMESTAMP DEFAULT current_timestamp NOT NULL,
                                      FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE ON UPDATE CASCADE
);
create table customer (
    id serial primary key,
    first_name varchar(200),
    last_name varchar(200),
    email varchar(200),
    password varchar(200)
    );

create table "order"(
  id serial primary key,
  create_at timestamp default current_timestamp,
  product int check (product > 0 ),
  price float ,
  customer_id int not null ,
  foreign key (customer_id) references customer(id) on update cascade on delete  cascade
);