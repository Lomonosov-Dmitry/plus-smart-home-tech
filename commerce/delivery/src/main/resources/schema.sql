CREATE TABLE IF NOT EXISTS deliveries (
    delivery_id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    order_id UUID,
    from_country VARCHAR,
    from_city VARCHAR,
    from_street VARCHAR,
    from_house VARCHAR,
    from_flat VARCHAR,
    to_country VARCHAR,
    to_city VARCHAR,
    to_street VARCHAR,
    to_house VARCHAR,
    to_flat VARCHAR,
    delivery_state VARCHAR,
    delivery_weight DECIMAL,
    delivery_volume DECIMAL,
    fragile BOOLEAN
);

