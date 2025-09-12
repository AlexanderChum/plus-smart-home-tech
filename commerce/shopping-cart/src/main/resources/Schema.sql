CREATE TABLE IF NOT EXISTS cart (
    cart_id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    username VARCHAR NOT NULL,
    state BOOLEAN NOT NULL
);

CREATE TABLE IF NOT EXISTS cart_products (
    product_id UUID NOT NULL,
    quantity BIGINT NOT NULL,
    cart_id UUID NOT NULL REFERENCES cart(cart_id) ON DELETE CASCADE
);