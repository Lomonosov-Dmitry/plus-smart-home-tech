CREATE TABLE IF NOT EXISTS carts (
    cart_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    username VARCHAR,
    active BOOLEAN,
    UNIQUE(cart_id, username)
);
CREATE TABLE IF NOT EXISTS product_cart (
    product_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    cart_id BIGINT REFERENCES carts(cart_id),
    product_name VARCHAR,
    quantity INT,
    UNIQUE (cart_id, product_name)
);

