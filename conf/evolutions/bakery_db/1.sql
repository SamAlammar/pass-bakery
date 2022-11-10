-- bakery_db schema

-- !Ups

CREATE TABLE IF NOT EXISTS Bakery
(
    id uuid NOT NULL DEFAULT gen_random_uuid(),
    name character varying COLLATE pg_catalog."default" NOT NULL,
    quantity integer,
    price double precision,
    "created at" timestamp without time zone,
    "updated at" timestamp without time zone,
    CONSTRAINT "bakery-inventory_pkey" PRIMARY KEY (id)
);

-- !Downs

DROP TABLE Bakery;
