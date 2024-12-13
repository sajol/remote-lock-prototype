CREATE TABLE product (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price DECIMAL(10, 2) NOT NULL
);

CREATE TABLE inventory (
    product_id INT REFERENCES product(id) PRIMARY KEY,
    stock INT NOT NULL DEFAULT 0,
    updated_at TIMESTAMP DEFAULT NOW()
);


-- SEED DATA ----
INSERT INTO product(name, description, price) VALUES
    ('Description for Product A', 19,99),
    ('Description for Product B', 29,99),
    ('Description for Product C', 39,99);

INSERT INTO inventory (product_id, stock) VALUES
    (1, 100),
    (2, 50),
    (3, 20);