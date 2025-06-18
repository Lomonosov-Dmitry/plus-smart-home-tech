CREATE TABLE IF NOT EXISTS warehouse_products (
    product_id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    fragile BOOLEAN,
    width DECIMAL,
    height DECIMAL,
    depth DECIMAL,
    weight DECIMAL,
    quantity INT
);
CREATE TABLE IF NOT EXISTS orders (
    order_id UUID PRIMARY KEY,
    delivery_id UUID,
    delivery_weight DECIMAL,
    delivery_volume DECIMAL,
    fragile BOOLEAN
);
