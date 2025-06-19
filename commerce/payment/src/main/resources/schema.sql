CREATE TABLE IF NOT EXISTS payments (
    payment_id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    order_id UUID,
    payment_state VARCHAR,
    total_payment DECIMAL,
    delivery_total DECIMAL,
    fee_total DECIMAL
);
