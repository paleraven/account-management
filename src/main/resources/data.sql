INSERT INTO customer (id, first_name, last_name) VALUES (1, 'Petr', 'Petrov');
INSERT INTO customer (id, first_name, last_name) VALUES (2, 'Ivan', 'Ivanov');

INSERT INTO account (id, customer_id, money) VALUES
  (1, 1, 500),
  (2, 1, 400),
  (3, 1, 300.50),
  (4, 2, 600),
  (5, 2, 700),
  (6, 2, 800);