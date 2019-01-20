CREATE SEQUENCE HIBERNATE_SEQUENCE AS INTEGER START WITH 100000;

CREATE TABLE customer
(
  id INTEGER GENERATED BY DEFAULT AS SEQUENCE HIBERNATE_SEQUENCE PRIMARY KEY,
  first_name VARCHAR(255),
  last_name VARCHAR(255)
);

CREATE TABLE account
(
  id INTEGER GENERATED BY DEFAULT AS SEQUENCE HIBERNATE_SEQUENCE PRIMARY KEY,
  money    float,
  customer_id INTEGER NOT NULL,
  FOREIGN KEY ( customer_id ) REFERENCES customer ( id ) ON DELETE CASCADE
);