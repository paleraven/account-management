CREATE TABLE customer
(
  id INTEGER PRIMARY KEY,
  first_name VARCHAR(255),
  last_name VARCHAR(255)
);

CREATE TABLE account
(
  id INTEGER PRIMARY KEY,
  money    float,
  customer_id INTEGER NOT NULL,
  FOREIGN KEY ( customer_id ) REFERENCES customer ( id ) ON DELETE CASCADE
);