-- src/main/resources/data.sql

-- Create admin user (password: admin123)
INSERT INTO users (name, email, password, role)
VALUES ('Admin User', 'admin@example.com', '$2a$10$xn3LI/AjqicFYZFruSwve.681477XaVNaUQbr1gioaWPn4t1KsnmG', 'ADMIN')
ON CONFLICT (email) DO NOTHING;

-- Create restaurant owner user (password: restaurant123)
INSERT INTO users (name, email, password, role)
VALUES ('Restaurant Owner', 'restaurant@example.com', '$2a$10$GQf4xFXP.mLJdzeqar2QUu3jnq7MqZBTAl8.TCZjK9XB75M/Hw/vi', 'RESTAURANT_STAFF')
ON CONFLICT (email) DO NOTHING;

-- Create delivery user (password: delivery123)
INSERT INTO users (name, email, password, role)
VALUES ('Delivery Person', 'delivery@example.com', '$2a$10$GMb6vSjCxWCdqLQeTtIr0OGNJUmlO8.eKN8DOqYLG3e5UQu9B8L4S', 'DELIVERY_PERSONNEL')
ON CONFLICT (email) DO NOTHING;

-- Create customer user (password: customer123)
INSERT INTO users (name, email, password, role)
VALUES ('Customer', 'customer@example.com', '$2a$10$KoW2W.E3/3X6HyVTwLS.qOQlnQl.qdP/dSqHBUTUY5Vs7S0FFsOlu', 'CUSTOMER')
ON CONFLICT (email) DO NOTHING;