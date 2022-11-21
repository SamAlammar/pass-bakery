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

CREATE OR REPLACE FUNCTION update_modified_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = now();;
    RETURN NEW;;
END;;
$$ language 'plpgsql';

CREATE TRIGGER update_product_modtime
    BEFORE UPDATE ON Product
    FOR EACH ROW
    EXECUTE PROCEDURE  update_modified_column();

CREATE OR REPLACE FUNCTION update_inserted_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.created_at = now();;
    RETURN NEW;;
END;;
$$ language 'plpgsql';

CREATE TRIGGER update_product_creatime
    BEFORE INSERT ON Product
    FOR EACH ROW
    EXECUTE PROCEDURE  update_inserted_column();

-- !Downs

DROP TABLE Product;
DROP FUNCTION update_modified_column();
DROP TRIGGER update_product_modtime;
DROP FUNCTION update_inserted_column();
DROP TRIGGER update_product_creatime;
