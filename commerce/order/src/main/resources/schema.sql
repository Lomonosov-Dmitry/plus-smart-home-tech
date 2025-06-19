CREATE TABLE IF NOT EXISTS orders (
    order_id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    shopping_cart_id UUID,
    payment_id UUID,
    delivery_id UUID,
    order_state VARCHAR,
    delivery_weight DECIMAL,
    delivery_volume DECIMAL,
    fragile BOOLEAN,
    total_price DECIMAL,
    delivery_price DECIMAL,
    product_price DECIMAL
);

CREATE TABLE IF NOT EXISTS products (
    product_id UUID PRIMARY KEY,
    order_id UUID REFERENCES orders(order_id),
    quantity INT
);

