CREATE TABLE IF NOT EXISTS carts (
    cart_id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    username VARCHAR,
    active BOOLEAN,
    UNIQUE(cart_id, username)
);
CREATE TABLE IF NOT EXISTS product_cart (
    product_id UUID PRIMARY KEY,
    cart_id UUID REFERENCES carts(cart_id),
    quantity INT
);

