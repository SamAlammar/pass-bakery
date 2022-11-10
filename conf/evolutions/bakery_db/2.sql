-- Products schema

-- !Ups

INSERT
  INTO Bakery (name, quantity, price)
  VALUES ('Glazed Donut', 2, 5);

INSERT
 INTO Bakery (name, quantity, price)
 VALUES ('English Bagel', 10, 2.3);


-- !Downs

DELETE FROM Bakery WHERE name = 'Glazed Donut'
DELETE FROM Bakery WHERE name = 'English Bagel'
