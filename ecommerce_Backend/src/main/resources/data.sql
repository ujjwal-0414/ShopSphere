-- E-Commerce demo seed data for the current Spring Boot project
-- Run after the application has created the categories/products tables.
-- IDs are intentionally not hard-coded; PostgreSQL generates them.
-- ShopSphere development reset + seed
-- WARNING: This deletes existing categories/products/orders/carts/payments/addresses.
-- Use only for development/testing.
TRUNCATE TABLE
    payments,
    order_items,
    orders,
    cart_items,
    carts,
    addresses,
    products,
    categories
RESTART IDENTITY CASCADE;

INSERT INTO categories (name, description, created_at, updated_at) VALUES
                                                                       ('Electronics', 'Smartphones, laptops, audio devices and technology accessories.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                       ('Books', 'Programming, software engineering, productivity and technology books.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                       ('Clothing', 'Everyday fashion including t-shirts, hoodies, jeans and jackets.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                       ('Home & Kitchen', 'Appliances, cookware, storage and everyday home products.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                       ('Beauty & Personal Care', 'Skincare, grooming and personal-care essentials.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                       ('Sports & Fitness', 'Workout equipment, fitness accessories and active lifestyle products.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                       ('Gaming', 'Gaming peripherals, controllers and desk accessories.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                       ('Bags & Accessories', 'Backpacks, wallets, watches and everyday carry accessories.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO products
(name, description, category_id, price, stock, image_url, created_at, updated_at)
VALUES
    ('Lenovo IdeaPad Slim 5', '15.6-inch laptop with AMD Ryzen processor, 16GB RAM and 512GB SSD for study, coding and everyday productivity.',
     (SELECT id FROM categories WHERE name='Electronics'), 64999.00, 15,
     'https://images.unsplash.com/photo-1496181133206-80ce9b88a853', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

    ('Logitech Wireless Mouse', 'Ergonomic wireless mouse with adjustable precision, quiet clicks and comfortable long-session control.',
     (SELECT id FROM categories WHERE name='Electronics'), 1299.00, 48,
     'https://resource.logitech.com/w_544%2Ch_466%2Car_7%3A6%2Cc_pad%2Cq_auto%2Cf_auto%2Cdpr_1.0/d_transparent.gif/content/dam/logitech/en/products/mice/mx-master-3s/migration-assets-for-delorean-2025/gallery/mx-master-3s-top-view-graphite.png', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

    ('Sony WH-1000XM5', 'Premium wireless noise-cancelling headphones with immersive sound, comfortable earcups and long battery life.',
     (SELECT id FROM categories WHERE name='Electronics'), 24999.00, 20,
     'https://www.sony.co.in/image/94101fcc4f07476f823d060b0a188f23?fmt=png-alpha&wid=1200', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

    ('Java Programming Masterclass', 'Comprehensive Java programming book covering OOP, collections, exceptions and modern Java features.',
     (SELECT id FROM categories WHERE name='Books'), 899.00, 39,
     'https://images.unsplash.com/photo-1512820790803-83ca734da794', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

    ('Samsung Galaxy A55 5G', '6.6-inch AMOLED smartphone with 5G connectivity, 8GB RAM and a versatile triple-camera setup.',
     (SELECT id FROM categories WHERE name='Electronics'), 34999.00, 22,
     'https://images.unsplash.com/photo-1511707171634-5f897ff02aa9', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

    ('Clean Code', 'Classic software engineering book focused on readable, maintainable and professional code.',
     (SELECT id FROM categories WHERE name='Books'), 1199.00, 30,
     'https://images.unsplash.com/photo-1544947950-fa07a98d237f', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

    ('Spring Boot in Action', 'Hands-on guide to building modern Spring Boot applications and REST APIs.',
     (SELECT id FROM categories WHERE name='Books'), 1099.00, 24,
     'https://images.unsplash.com/photo-1532012197267-da84d127e765', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

    ('Designing Data-Intensive Applications', 'Guide to reliable, scalable and maintainable data systems.',
     (SELECT id FROM categories WHERE name='Books'), 1599.00, 18,
     'https://images.unsplash.com/photo-1495446815901-a7297e633e8d', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

    ('Premium Cotton T-Shirt', 'Regular-fit cotton t-shirt with breathable fabric and a versatile everyday design.',
     (SELECT id FROM categories WHERE name='Clothing'), 799.00, 75,
     'https://images.unsplash.com/photo-1521572163474-6864f9cf17ab', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

    ('Classic Casual Hoodie', 'Warm everyday hoodie with comfortable fit and soft fleece interior.',
     (SELECT id FROM categories WHERE name='Clothing'), 1499.00, 45,
     'https://images.unsplash.com/photo-1556821840-3a63f95609a7', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

    ('Slim Fit Blue Jeans', 'Classic slim-fit denim jeans designed for everyday wear.',
     (SELECT id FROM categories WHERE name='Clothing'), 1999.00, 38,
     'https://images.unsplash.com/photo-1542272604-787c3835535d', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

    ('Lightweight Casual Jacket', 'Minimal casual jacket suitable for travel and everyday use.',
     (SELECT id FROM categories WHERE name='Clothing'), 2499.00, 25,
     'https://images.unsplash.com/photo-1551488831-00ddcb6c6bd3', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

    ('Electric Kettle 1.5L', 'Fast-boiling stainless-steel electric kettle with automatic shut-off.',
     (SELECT id FROM categories WHERE name='Home & Kitchen'), 1599.00, 35,
     'https://images.unsplash.com/photo-1594213114663-d94db9b1719f', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

    ('Non-Stick Cookware Set', 'Multi-piece non-stick cookware set for everyday cooking.',
     (SELECT id FROM categories WHERE name='Home & Kitchen'), 3299.00, 20,
     'https://images.unsplash.com/photo-1556911220-e15b29be8c8f', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

    ('Bamboo Storage Organizer', 'Multi-compartment bamboo organizer for desks, shelves and kitchen spaces.',
     (SELECT id FROM categories WHERE name='Home & Kitchen'), 899.00, 32,
     'https://images.unsplash.com/photo-1586023492125-27b2c045efd7', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

    ('Stainless Steel Water Bottle', 'Insulated stainless-steel bottle designed to keep drinks hot or cold.',
     (SELECT id FROM categories WHERE name='Home & Kitchen'), 999.00, 50,
     'https://images.unsplash.com/photo-1602143407151-7111542de6e8', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

    ('Vitamin C Face Serum', 'Lightweight skincare serum for everyday skincare routines.',
     (SELECT id FROM categories WHERE name='Beauty & Personal Care'), 699.00, 42,
     'https://images.unsplash.com/photo-1556228578-8c89e6adf883', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

    ('Moisturizing Face Cream', 'Daily moisturizing cream with a lightweight texture.',
     (SELECT id FROM categories WHERE name='Beauty & Personal Care'), 549.00, 55,
     'https://images.unsplash.com/photo-1611930022073-b7a4ba5fcccd', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

    ('Beard Grooming Kit', 'Grooming kit with beard oil, comb, scissors and styling accessories.',
     (SELECT id FROM categories WHERE name='Beauty & Personal Care'), 899.00, 30,
     'https://images.unsplash.com/photo-1621605815971-fbc98d665033', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

    ('Yoga Mat 6mm', 'Non-slip exercise mat with cushioned support for yoga and workouts.',
     (SELECT id FROM categories WHERE name='Sports & Fitness'), 999.00, 48,
     'https://images.unsplash.com/photo-1601925260368-ae2f83cf8b7f', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

    ('Adjustable Dumbbell Set', 'Space-saving adjustable dumbbells for home strength training.',
     (SELECT id FROM categories WHERE name='Sports & Fitness'), 4999.00, 16,
     'https://images.unsplash.com/photo-1583454110551-21f2fa2afe61', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

    ('Stainless Steel Shaker Bottle', 'Leak-resistant shaker bottle for protein shakes and workout drinks.',
     (SELECT id FROM categories WHERE name='Sports & Fitness'), 699.00, 60,
     'https://images.unsplash.com/photo-1593095948071-474c5cc2989d', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

    ('Wireless Gaming Controller', 'Ergonomic wireless controller with responsive buttons and dual vibration.',
     (SELECT id FROM categories WHERE name='Gaming'), 2999.00, 25,
     'https://images.unsplash.com/photo-1592840496694-26d035b52b48', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

    ('RGB Gaming Headset', 'Over-ear gaming headset with microphone and RGB lighting.',
     (SELECT id FROM categories WHERE name='Gaming'), 3499.00, 27,
     'https://images.unsplash.com/photo-1599669454699-248893623440', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

    ('Large Gaming Mouse Pad', 'Extended desk mat with smooth tracking surface and stitched edges.',
     (SELECT id FROM categories WHERE name='Gaming'), 1299.00, 45,
     'https://images.unsplash.com/photo-1615663245857-ac93bb7c39e7', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

    ('Water-Resistant Laptop Backpack', 'Spacious laptop backpack with padded laptop compartment and organizer pockets.',
     (SELECT id FROM categories WHERE name='Bags & Accessories'), 1899.00, 35,
     'https://images.unsplash.com/photo-1553062407-98eeb64c6a62', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

    ('Minimal Leather Wallet', 'Slim everyday wallet with multiple card slots and compact bifold design.',
     (SELECT id FROM categories WHERE name='Bags & Accessories'), 999.00, 50,
     'https://images.unsplash.com/photo-1627123424574-724758594e93', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

    ('Classic Analog Watch', 'Minimal analog wristwatch with a clean dial and versatile everyday styling.',
     (SELECT id FROM categories WHERE name='Bags & Accessories'), 2299.00, 22,
     'https://images.unsplash.com/photo-1524805444758-089113d48a6d', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

    ('Anker PowerCore Power Bank', 'Portable high-capacity power bank with fast charging support.',
     (SELECT id FROM categories WHERE name='Electronics'), 2999.00, 40,
     'https://images.unsplash.com/photo-1609592424733-2f9b1c0c4d85', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

    ('Mechanical Keyboard RGB', 'Compact mechanical keyboard with tactile switches and RGB backlighting.',
     (SELECT id FROM categories WHERE name='Electronics'), 4499.00, 28,
     'https://images.unsplash.com/photo-1587829741301-dc798b83add3', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);