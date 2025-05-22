CREATE TABLE IF NOT EXISTS products (
    product_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    product_name VARCHAR,
    description VARCHAR,
    image_src VARCHAR,
    quantity_state VARCHAR,
    product_state VARCHAR,
    product_category VARCHAR,
    price DECIMAL
);

