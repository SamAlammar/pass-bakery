-- bakery_db schema

-- !Ups

CREATE TABLE IF NOT EXISTS Product
(
    id uuid NOT NULL DEFAULT gen_random_uuid(),
    name character varying COLLATE pg_catalog."default" NOT NULL,
    quantity integer,
    price double precision,
    "created_at" timestamp without time zone,
    "updated_at" timestamp without time zone,
    CONSTRAINT "bakery-inventory_pkey" PRIMARY KEY (id)
);

-- !Downs

DROP TABLE Product;
