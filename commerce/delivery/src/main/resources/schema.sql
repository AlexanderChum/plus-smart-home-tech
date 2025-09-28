CREATE TABLE IF NOT EXISTS delivery (
    delivery_id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    from_address_id UUID NOT NULL REFERENCES address(address_id),
    to_address_id UUID NOT NULL REFERENCES address(address_id),
    order_id UUID NOT NULL,
    delivery_state VARCHAR NOT NULL
);