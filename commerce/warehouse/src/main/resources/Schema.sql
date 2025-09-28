CREATE TABLE IF NOT EXISTS products_warehouse (
    product_id UUID PRIMARY KEY,
    fragile BOOLEAN,
    width DOUBLE PRECISION NOT NULL,
    height DOUBLE PRECISION NOT NULL,
    depth DOUBLE PRECISION NOT NULL,
    weight DOUBLE PRECISION,
    quantity BIGINT
);

CREATE TABLE IF NOT EXISTS order_booking (
    booking_id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    order_id UUID UNIQUE,
    delivery_id UNIQUE
);

CREATE TABLE IF NOT EXISTS order_products (
    booking_id UUID PRIMARY KEY REFERENCES order_booking(booking_id),
    product_id UUID REFERENCES products_warehouse(product_id),
    quantity BIGINT NOT NULL
);