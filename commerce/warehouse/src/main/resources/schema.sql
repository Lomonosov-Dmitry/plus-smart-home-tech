CREATE TABLE IF NOT EXISTS warehouse_products (
    product_id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    fragile BOOLEAN,
    width DECIMAL,
    height DECIMAL,
    depth DECIMAL,
    weight DECIMAL,
    quantity INT
);

