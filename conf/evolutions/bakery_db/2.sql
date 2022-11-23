-- Products schema

-- !Ups

INSERT
  INTO Product (name, quantity, price)
  VALUES ('Glazed Donut', 2, 5);

INSERT
 INTO Product (name, quantity, price)
 VALUES ('English Bagel', 10, 2.3);


-- !Downs

DELETE FROM Product WHERE name = 'Glazed Donut';
DELETE FROM Product WHERE name = 'English Bagel';
