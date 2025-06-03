create DATABASE  Shop_Quan_Ao; 
USE Shop_Quan_Ao;
GO

-- 1. USERS
CREATE TABLE users (
    id INT PRIMARY KEY IDENTITY,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    full_name NVARCHAR(100),
    role VARCHAR(20) DEFAULT 'user',
    facebook_id VARCHAR(100) UNIQUE NULL,
    google_id   VARCHAR(100) UNIQUE NULL,
    address       NVARCHAR(500) NULL,
    created_at DATETIME DEFAULT GETDATE()
);


-- 2. PRODUCTS
CREATE TABLE products (
    id INT PRIMARY KEY IDENTITY,
    name NVARCHAR(100) NOT NULL,
    description NVARCHAR(1000),
    price DECIMAL(10,2) NOT NULL,
    category NVARCHAR(50),
	status bit not null default 1,
    created_at DATETIME DEFAULT GETDATE()
);
CREATE INDEX idx_products_category ON products(category);

CREATE TABLE product_variants (
    id INT PRIMARY KEY IDENTITY,
    product_id INT FOREIGN KEY REFERENCES products(id),
    size NVARCHAR(10) NOT NULL,               -- ví dụ: 'S', 'M', 'L', 'XL'
    quantity INT DEFAULT 0 NOT NULL,          -- số lượng còn lại
    UNIQUE (product_id, size)                 -- tránh lặp size cho cùng 1 sản phẩm
);
-- 3. PRODUCT_IMAGES
CREATE TABLE product_images (
    id INT PRIMARY KEY IDENTITY,
    product_id INT NOT NULL FOREIGN KEY REFERENCES products(id),
    image_url NVARCHAR(255),
    is_primary BIT DEFAULT 0
);

-- 4. ORDERS
CREATE TABLE orders (
    id INT PRIMARY KEY IDENTITY,
    user_id INT NOT NULL FOREIGN KEY REFERENCES users(id),
    order_date DATETIME DEFAULT GETDATE(),
    total_amount DECIMAL(10,2),
    payment_status VARCHAR(20)  DEFAULT 'Pending',
    order_status   VARCHAR(20)  DEFAULT 'Pending',
    payment_method VARCHAR(50)  NULL,
    transaction_id VARCHAR(100) NULL
);

-- 5. ORDER_ITEMS
CREATE TABLE order_items (
    id INT PRIMARY KEY IDENTITY,
    order_id   INT NOT NULL FOREIGN KEY REFERENCES orders(id),
	variant_id INT FOREIGN KEY REFERENCES product_variants(id),
    quantity   INT NOT NULL,
    unit_price DECIMAL(10,2) NOT NULL
);

-- 6. CART_ITEMS
CREATE TABLE cart_items (
    id         INT PRIMARY KEY IDENTITY,
    user_id    INT NOT NULL FOREIGN KEY REFERENCES users(id) ON DELETE CASCADE,
	variant_id INT FOREIGN KEY REFERENCES product_variants(id),
    quantity   INT NOT NULL,
    added_at   DATETIME DEFAULT GETDATE()
);

-- 7. PRODUCT_VIEWS
CREATE TABLE product_views (
    id INT PRIMARY KEY IDENTITY,
    user_id    INT NOT NULL FOREIGN KEY REFERENCES users(id),
    product_id INT NOT NULL FOREIGN KEY REFERENCES products(id),
    view_time  DATETIME DEFAULT GETDATE()
);

-- 8. RECOMMENDATIONS
CREATE TABLE recommendations (
    id                     INT PRIMARY KEY IDENTITY,
    user_id                INT NOT NULL FOREIGN KEY REFERENCES users(id),
    recommended_product_id INT NOT NULL FOREIGN KEY REFERENCES products(id),
    score                  FLOAT,
    algorithm              VARCHAR(50) DEFAULT 'collaborative',
    created_at             DATETIME DEFAULT GETDATE()
);

-- 9. CHATBOT_LOGS
CREATE TABLE chatbot_logs (
    id          INT PRIMARY KEY IDENTITY,
    user_id     INT NOT NULL FOREIGN KEY REFERENCES users(id),
    question    NVARCHAR(1000),
    ai_response NVARCHAR(1000),
    created_at  DATETIME DEFAULT GETDATE()
);
-- 10. REVENUE
CREATE TABLE revenue_forecast (
  id INT IDENTITY PRIMARY KEY,
  month VARCHAR(7) NOT NULL,          -- '2025-06'
  predicted_revenue DECIMAL(12,2) NOT NULL,
  method VARCHAR(20)   NOT NULL,      -- 'MA3'
  created_at DATETIME DEFAULT GETDATE()
);
-- 11. PROMOTION
CREATE TABLE promotions (
  id INT PRIMARY KEY IDENTITY,
  promo_code      VARCHAR(50) UNIQUE NOT NULL,   -- Mã hoặc tên chương trình
  description     NVARCHAR(255)    NULL,
  start_date      DATETIME         NOT NULL,
  end_date        DATETIME         NOT NULL,
  discount_type   VARCHAR(20)      NOT NULL,    -- 'percentage' hoặc 'fixed_amount'
  discount_value  DECIMAL(10,2)    NOT NULL,    -- Nếu percentage thì lưu 10 = 10%; nếu fixed_amount, lưu ví dụ 100000 = 100k VND
  condition_threshold DECIMAL(12,2) NULL,        -- Ví dụ: >= 500000 để được áp dụng
  applicable_category_id NVARCHAR(50)       NULL,         -- Nếu khuyến mãi cho 1 category (FK sang bảng categories, nếu có)
  applicable_product_id  INT       NULL,         -- Nếu khuyến mãi cho 1 product (FK sang products)
  status          VARCHAR(20)      DEFAULT 'active',  -- 'active', 'expired', 'pending'
  created_at      DATETIME         DEFAULT GETDATE()
);
-- 12. business_reports
CREATE TABLE business_reports (
  id            INT PRIMARY KEY IDENTITY,
  report_date   DATE         NOT NULL,      -- Ngày agent chạy và tạo report
  report_type   VARCHAR(50)  NOT NULL,      -- 'weekly', 'monthly', 'advice'
  content       NVARCHAR(MAX) NOT NULL,     -- JSON hoặc text tóm tắt đề xuất
  generated_by  VARCHAR(50)  DEFAULT 'advisor_agent',
  status        VARCHAR(20)  DEFAULT 'new', -- 'new', 'reviewed', 'executed'
  created_at    DATETIME     DEFAULT GETDATE()
);
-- 13. action_log
CREATE TABLE action_logs (
  id          INT PRIMARY KEY IDENTITY,
  report_id   INT     NOT NULL FOREIGN KEY REFERENCES business_reports(id),
  action_type VARCHAR(50) NOT NULL,         -- 'create_facebook_campaign', 'apply_discount', v.v.
  status      VARCHAR(20) DEFAULT 'pending', -- 'pending', 'success', 'failed'
  payload     NVARCHAR(MAX) NOT NULL,        -- JSON input cho hành động
  response    NVARCHAR(MAX) NULL,            -- JSON phản hồi từ API (nếu có)
  executed_at DATETIME DEFAULT GETDATE()
);

go


INSERT INTO products (name, description, price, category)
VALUES
-- Accessories
('black sock', 'Home', 79.99, 'Jersey'),
('white sock', 'Home', 79.99, 'Jersey'),
('black short', 'Home', 59.99, 'Jersey'),
('white short', 'Home', 79.99, 'Jersey'),
('short sleeve shirt', 'Home', 79.99, 'Jersey'),
('long sleeve shirt', 'Home', 59.99, 'Jersey'),

-- Player Jerseys (MU 2024/2025 - Home)
('Rasmus Højlund', 'Home', 79.99, 'Jersey'),
('Bruno Fernandes', 'Home', 79.99, 'Jersey'),
('Alejandro Garnacho', 'Home', 79.99, 'Jersey'),
('Kobbie Mainoo', 'Home', 79.99, 'Jersey'),
('Lisandro Martínez', 'Home', 79.99, 'Jersey'),
('Marcus Rashford', 'Home', 79.99, 'Jersey'),
('Patrick Dorgu', 'Home', 79.99, 'Jersey'),
('Amad Diallo', 'Home', 79.99, 'Jersey'),
('Matthijs de Ligt', 'Home', 79.99, 'Jersey'),
('Leny Yoro', 'Home', 79.99, 'Jersey'),
('Joshua Zirkzee', 'Home', 79.99, 'Jersey'),



('white sock', 'Away', 79.99, 'Jersey'),
('blue sock', 'Away', 79.99, 'Jersey'),
('white short', 'Away', 79.99, 'Jersey'),
('blue short', 'Away', 79.99, 'Jersey'),
('short sleeve shirt', 'Away', 79.99, 'Jersey'),
('long sleeve shirt', 'Away', 59.99, 'Jersey'),

-- Player Jerseys (MU 2024/2025 - Away)
('Rasmus Højlund', 'Away', 79.99, 'Jersey'),
('Bruno Fernandes', 'Away', 79.99, 'Jersey'),
('Alejandro Garnacho', 'Away', 79.99, 'Jersey'),
('Kobbie Mainoo', 'Away', 79.99, 'Jersey'),
('Lisandro Martínez', 'Away', 79.99, 'Jersey'),
('Marcus Rashford', 'Away', 79.99, 'Jersey'),
('Patrick Dorgu', 'Away', 79.99, 'Jersey'),
('Amad Diallo', 'Away', 79.99, 'Jersey'),
('Matthijs de Ligt', 'Away', 79.99, 'Jersey'),
('Leny Yoro', 'Away', 79.99, 'Jersey'),
('Joshua Zirkzee', 'Away', 79.99, 'Jersey'),

-- Accessories
('black sock', 'Third', 79.99, 'Jersey'),
('white sock', 'Third', 79.99, 'Jersey'),
('black short', 'Third', 59.99, 'Jersey'),
('white short', 'Third', 79.99, 'Jersey'),
('short sleeve shirt', 'Third', 79.99, 'Jersey'),
('long sleeve shirt', 'Third', 59.99, 'Jersey'),

-- Player Jerseys (MU 2024/2025 - Third)
('Rasmus Højlund', 'Third', 79.99, 'Jersey'),
('Bruno Fernandes', 'Third', 79.99, 'Jersey'),
('Alejandro Garnacho', 'Third', 79.99, 'Jersey'),
('Kobbie Mainoo', 'Third', 79.99, 'Jersey'),
('Lisandro Martínez', 'Third', 79.99, 'Jersey'),
('Marcus Rashford', 'Third', 79.99, 'Jersey'),
('Patrick Dorgu', 'Third', 79.99, 'Jersey'),
('Amad Diallo', 'Third', 79.99, 'Jersey'),
('Matthijs de Ligt', 'Third', 79.99, 'Jersey'),
('Leny Yoro', 'Third', 79.99, 'Jersey'),
('Joshua Zirkzee', 'Third', 79.99, 'Jersey'),

('David Degea', 'Goalkeeper', 69.99, 'Jersey'),
('long sleeve violet shirt', 'Goalkeeper', 59.99, 'Jersey'),
('violet sock', 'Goalkeeper', 49.99, 'Jersey'),
('violet short', 'Goalkeeper', 59.99, 'Jersey'),
('long sleeve black shirt', 'Goalkeeper', 59.99, 'Jersey'),
('black sock', 'Goalkeeper', 49.99, 'Jersey'),
('black short', 'Goalkeeper', 59.99, 'Jersey');



INSERT INTO products (name, description, price, category)
VALUES
-- Top (12)
('MU Training Tee White', 'Top', 49.99, 'Trainingwear'),
('MU Training Tee Black', 'Top', 49.99, 'Trainingwear'),
('MU Training Tee Grey', 'Top', 49.99, 'Trainingwear'),
('MU Training Tee Red Trim', 'Top', 49.99, 'Trainingwear'),
('MU Training Tee Contrast Sleeve', 'Top', 49.99, 'Trainingwear'),
('MU Training Tee Half Zip', 'Top', 49.99, 'Trainingwear'),
('MU Training Long Sleeve Black', 'Top', 59.99, 'Trainingwear'),
('MU Training Long Sleeve White', 'Top', 59.99, 'Trainingwear'),
('MU Training Base Tee Grey', 'Top', 39.99, 'Trainingwear'),
('MU Training Top Performance', 'Top', 59.99, 'Trainingwear'),
('MU Training Base Tee Black', 'Top', 39.99, 'Trainingwear'),
('MU Training Tee Slim Fit', 'Top', 49.99, 'Trainingwear'),

-- Jackets & Coats (9)
('MU Training Zip Jacket Red', 'Jackets & Coats', 69.99, 'Trainingwear'),
('MU Training Zip Jacket Navy', 'Jackets & Coats', 69.99, 'Trainingwear'),
('MU Training Anthem Jacket', 'Jackets & Coats', 74.99, 'Trainingwear'),
('MU Training Windbreaker Black', 'Jackets & Coats', 74.99, 'Trainingwear'),
('MU Training Travel Jacket', 'Jackets & Coats', 74.99, 'Trainingwear'),
('MU Training Full Zip Grey', 'Jackets & Coats', 74.99, 'Trainingwear'),
('MU Training Pre-Match Jacket', 'Jackets & Coats', 74.99, 'Trainingwear'),
('MU Training Rain Jacket', 'Jackets & Coats', 79.99, 'Trainingwear'),
('MU Training All Weather Jacket', 'Jackets & Coats', 79.99, 'Trainingwear'),

-- Shorts & Trousers (10)
('MU Training Shorts Black', 'Shorts & Trousers', 39.99, 'Trainingwear'),
('MU Training Shorts White', 'Shorts & Trousers', 39.99, 'Trainingwear'),
('MU Training Shorts Grey', 'Shorts & Trousers', 39.99, 'Trainingwear'),
('MU Training Shorts Slim Fit', 'Shorts & Trousers', 39.99, 'Trainingwear'),
('MU Training Shorts Red Trim', 'Shorts & Trousers', 39.99, 'Trainingwear'),
('MU Training Pants Black', 'Shorts & Trousers', 54.99, 'Trainingwear'),
('MU Training Pants Red Line', 'Shorts & Trousers', 54.99, 'Trainingwear'),
('MU Training Track Pants', 'Shorts & Trousers', 54.99, 'Trainingwear'),
('MU Training Joggers Logo', 'Shorts & Trousers', 54.99, 'Trainingwear'),
('MU Training Leggings Black', 'Shorts & Trousers', 49.99, 'Trainingwear');

INSERT INTO products (name, category, price, description) VALUES
('Essential Black Tee', 'Fashion', 24.99, 'Tshirt'),
('Classic White Polo', 'Fashion', 24.99, 'Tshirt'),
('Street Black Fit', 'Fashion', 24.99, 'Tshirt'),
('Urban White Set', 'Fashion', 24.99, 'Tshirt'),
('Camo Grey Tee', 'Fashion', 24.99, 'Tshirt'),
('Ash Zip Collar Tee', 'Fashion', 24.99, 'Tshirt'),
('Monochrome Wave', 'Fashion', 24.99, 'Tshirt'),
('Stone Casual Tee', 'Fashion', 24.99, 'Tshirt'),
('Red Icon Tee', 'Fashion', 24.99, 'Tshirt'),
('White Signature Tee', 'Fashion', 24.99, 'Tshirt'),
('Retro Graphic Tee', 'Fashion', 24.99, 'Tshirt'),
('Sky Blue Polo', 'Fashion', 24.99, 'Tshirt'),
('Bold Crest Black Tee', 'Fashion', 24.99, 'Tshirt'),
('Royal Blue Fit', 'Fashion', 24.99, 'Tshirt'),
('Navy Game Polo', 'Fashion', 24.99, 'Tshirt'),
('Olive Crew Tee', 'Fashion', 24.99, 'Tshirt'),
('White Daily Polo', 'Fashion', 24.99, 'Tshirt'),
('Deep Maroon Polo', 'Fashion', 24.99, 'Tshirt'),
('Forest Green Badge Tee', 'Fashion', 24.99, 'Tshirt'),
('Pastel Yellow Tee', 'Fashion', 24.99, 'Tshirt'),
('Summer Lilac Tee', 'Fashion', 24.99, 'Tshirt'),
('Athleisure Pink Tee', 'Fashion', 24.99, 'Tshirt'),
('Casual White Badge Tee', 'Fashion', 24.99, 'Tshirt'),
('Street Maroon Polo', 'Fashion', 24.99, 'Tshirt'),
('Classic Crest Red Tee', 'Fashion', 24.99, 'Tshirt'),
('Heritage Label Tee', 'Fashion', 24.99, 'Tshirt'),
('Cotton Club Black Tee', 'Fashion', 24.99, 'Tshirt'),
('Graphic Number Tee', 'Fashion', 24.99, 'Tshirt'),
('Manchester Red Tee', 'Fashion', 24.99, 'Tshirt'),
('City Style Black Tee', 'Fashion', 24.99, 'Tshirt'),
('Bright Red Polo', 'Fashion', 24.99, 'Tshirt'),
('Contrast Edge Tee', 'Fashion', 24.99, 'Tshirt'),
('Cotton Heritage Tee', 'Fashion', 24.99, 'Tshirt'),
('Green Daily Tee', 'Fashion', 24.99, 'Tshirt'),
('True Black Tee', 'Fashion', 24.99, 'Tshirt'),
('White Logo Tee', 'Fashion', 24.99, 'Tshirt'),
('Core Red Polo', 'Fashion', 24.99, 'Tshirt'),
('White Daily Tee', 'Fashion', 24.99, 'Tshirt'),
('Navy Slim Polo', 'Fashion', 24.99, 'Tshirt'),
('Dream Backprint Tee', 'Fashion', 24.99, 'Tshirt'),
('Blackout Modern Tee', 'Fashion', 24.99, 'Tshirt'),
('1878 Heritage Tee', 'Fashion', 24.99, 'Tshirt'),
('Olive Urban Tee', 'Fashion', 24.99, 'Tshirt'),
('Team Logo White Tee', 'Fashion', 24.99, 'Tshirt'),
('Vintage Gradient Tee', 'Fashion', 24.99, 'Tshirt'),
('Minimal Clay Tee', 'Fashion', 24.99, 'Tshirt'),
('Backline Clay Tee', 'Fashion', 24.99, 'Tshirt');
 
 

 INSERT INTO products (name, category, price, description) VALUES
('Shadow Stripe Hoodie', 'Fashion', 49.99, 'Hoodie'),
('Urban Motion Hoodie', 'Fashion', 49.99, 'Hoodie'),
('Velocity Zip Hoodie', 'Fashion', 49.99, 'Hoodie'),
('Midnight Crest Hoodie', 'Fashion', 49.99, 'Hoodie'),
('Navy Classic Hoodie', 'Fashion', 49.99, 'Hoodie'),
('Royal Zip Hoodie', 'Fashion', 49.99, 'Hoodie'),
('Graphite Street Hoodie', 'Fashion', 49.99, 'Hoodie'),
('Smoky Grey Pullover', 'Fashion', 49.99, 'Hoodie'),
('Red Label Hoodie', 'Fashion', 49.99, 'Hoodie'),
('Crimson Bold Hoodie', 'Fashion', 49.99, 'Hoodie'),
('Mono Fit Hoodie', 'Fashion', 49.99, 'Hoodie'),
('Statement Crest Hoodie', 'Fashion', 49.99, 'Hoodie'),
('Dark Print Hoodie', 'Fashion', 49.99, 'Hoodie'),
('Ivory Essential Hoodie', 'Fashion', 49.99, 'Hoodie'),
('Back Patch Hoodie', 'Fashion', 49.99, 'Hoodie'),
('Bright Red Sport Hoodie', 'Fashion', 49.99, 'Hoodie'),
('Dream Motion Hoodie', 'Fashion', 49.99, 'Hoodie'),
('Classic Zip Hoodie', 'Fashion', 49.99, 'Hoodie'),
('Minimal Line Hoodie', 'Fashion', 49.99, 'Hoodie'),
('Rose Dust Hoodie', 'Fashion', 49.99, 'Hoodie'),
('Essential Rain Hoodie', 'Fashion', 49.99, 'Hoodie'),
('Red Chest Hoodie', 'Fashion', 49.99, 'Hoodie'),
('Athletic Navy Hoodie', 'Fashion', 49.99, 'Hoodie'),
('Retro Track Hoodie', 'Fashion', 49.99, 'Hoodie'),
('Pine Green Badge Hoodie', 'Fashion', 49.99, 'Hoodie'),
('Black Performance Hoodie', 'Fashion', 49.99, 'Hoodie'),
('Maroon Street Hoodie', 'Fashion', 49.99, 'Hoodie'),
('Night Drive Hoodie', 'Fashion', 49.99, 'Hoodie'),
('Lemon Zip Hoodie', 'Fashion', 49.99, 'Hoodie'),
('Classic Training Hoodie', 'Fashion', 49.99, 'Hoodie'),
('Grey Game Hoodie', 'Fashion', 49.99, 'Hoodie'),
('Indigo Washed Hoodie', 'Fashion', 49.99, 'Hoodie'),
('Mint Club Hoodie', 'Fashion', 49.99, 'Hoodie'),
('Grey Jogger Set', 'Fashion', 49.99, 'Hoodie'),
('Flame Zip Hoodie', 'Fashion', 49.99, 'Hoodie'),
('White Bold Hoodie', 'Fashion', 49.99, 'Hoodie'),
('Red Daily Fit Hoodie', 'Fashion', 49.99, 'Hoodie'),
('Black Warm Hoodie', 'Fashion', 49.99, 'Hoodie'),
('Classic Red Polo Hoodie', 'Fashion', 49.99, 'Hoodie'),
('Navy Icon Hoodie', 'Fashion', 49.99, 'Hoodie'),
('True Black Crest Hoodie', 'Fashion', 49.99, 'Hoodie'),
('Grey Bold Front Hoodie', 'Fashion', 49.99, 'Hoodie'),
('Red Strike Hoodie', 'Fashion', 49.99, 'Hoodie'),
('Modern Zip Hoodie', 'Fashion', 49.99, 'Hoodie'),
('Trackline Pro Hoodie', 'Fashion', 49.99, 'Hoodie'),
('Grey Side Zip Hoodie', 'Fashion', 49.99, 'Hoodie'),
('Sport Luxe Hoodie', 'Fashion', 49.99, 'Hoodie'),
('Retro Zip Hoodie', 'Fashion', 49.99, 'Hoodie');


INSERT INTO products (name, category, price, description) VALUES
('MU Retro Blue Graphic Tee', 'Fashion', 49.99, 'Retro'),
('MU 1994 Blue Striped Home Kit', 'Fashion', 49.99, 'Retro'),
('MU Minimal White Tee', 'Fashion', 49.99, 'Retro'),
('MU Classic White V-Collar Jersey', 'Fashion', 49.99, 'Retro'),
('MU Number 7 Retro Jersey', 'Fashion', 49.99, 'Retro'),
('MU Red Polo Shirt', 'Fashion', 49.99, 'Retro'),
('MU Blue Sharp Sweatshirt', 'Fashion', 49.99, 'Retro'),
('MU Red Sharp Jersey', 'Fashion', 49.99, 'Retro'),
('MU Black Retro Polo', 'Fashion', 49.99, 'Retro'),
('MU White Collar Sharp Tee', 'Fashion', 49.99, 'Retro'),
('MU Red Sharp Training Tee', 'Fashion', 49.99, 'Retro'),
('MU Green Keeper Kit', 'Fashion', 49.99, 'Retro'),
('MU Red Track Jacket', 'Fashion', 49.99, 'Retro'),
('MU Red-White Training Jacket', 'Fashion', 49.99, 'Retro'),
('MU Black-White Panel Jacket', 'Fashion', 49.99, 'Retro'),
('MU White Zip Hoodie with Red Stripe', 'Fashion', 49.99, 'Retro');

INSERT INTO products (name, category, price, description) VALUES
('MU Red Plaid Pajama Set', 'Fashion', 39.99, 'Nightwear'),
('MU Red Vertical Stripe Pajama Set', 'Fashion', 39.99, 'Nightwear'),
('MU Black Long Sleeve Pajama Set', 'Fashion', 39.99, 'Nightwear'),
('MU Maroon Tee with Plaid Bottoms', 'Fashion', 39.99, 'Nightwear'),
('MU White Tee with Black Shorts', 'Fashion', 39.99, 'Nightwear'),
('MU Red Oversized Hoodie Pajama Set', 'Fashion', 39.99, 'Nightwear'),
('MU Black Oversized Hoodie Pajama Set', 'Fashion', 39.99, 'Nightwear'),
('MU Classic Gray Button-Up Pajama Set', 'Fashion', 39.99, 'Nightwear');

INSERT INTO products (name, category, price, description) VALUES
('MU Olive Jogger and Sweatpants Set', 'Fashion', 39.99, 'Shorts and pants'),
('MU Mint Green Jogger and Sweatpants Set', 'Fashion', 39.99, 'Shorts and pants'),
('MU Black and White Stripe Shorts and Pants Set', 'Fashion', 39.99, 'Shorts and pants'),
('MU Denim Tracksuit Shorts and Pants Set', 'Fashion', 39.99, 'Shorts and pants'),
('MU Black & White Minimalist Shorts and Pants Set', 'Fashion', 39.99, 'Shorts and pants'),
('MU Casual Black Shorts and Joggers Set', 'Fashion', 39.99, 'Shorts and pants'),
('MU Red Tee Shorts and Joggers Set', 'Fashion', 39.99, 'Shorts and pants'),
('MU Black Shorts with Logo and Pants Set', 'Fashion', 39.99, 'Shorts and pants'),
('MU White Tee Black Shorts and Pants Set', 'Fashion', 39.99, 'Shorts and pants'),
('MU Navy Shorts and Joggers Set', 'Fashion', 39.99, 'Shorts and pants'),
('MU Gray Pants with Side Stripes Set', 'Fashion', 39.99, 'Shorts and pants'),
('MU Essentials Gray Shorts and Joggers Set', 'Fashion', 39.99, 'Shorts and pants'),
('MU Blue Tee and Navy Shorts Set', 'Fashion', 39.99, 'Shorts and pants'),
('MU Manchester United Shorts and Pants Set', 'Fashion', 39.99, 'Shorts and pants'),
('MU Navy Training Shorts and Joggers Set', 'Fashion', 39.99, 'Shorts and pants'),
('MU Blue Hoodie Shorts and Pants Set', 'Fashion', 39.99, 'Shorts and pants'),
('MU Oversized Black Shorts and Joggers Set', 'Fashion', 39.99, 'Shorts and pants'),
('MU Red Graphic Tee Shorts and Pants Set', 'Fashion', 39.99, 'Shorts and pants'),
('MU Essentials Navy Shorts and Joggers Set', 'Fashion', 39.99, 'Shorts and pants'),
('MU Blue Track Shorts and Joggers Set', 'Fashion', 39.99, 'Shorts and pants'),
('MU Black and Red Sports Shorts and Pants Set', 'Fashion', 39.99, 'Shorts and pants'),
('MU Gray Athletic Shorts and Pants Set', 'Fashion', 39.99, 'Shorts and pants'),
('MU Striped Navy Shorts and Joggers Set', 'Fashion', 39.99, 'Shorts and pants'),
('MU Black Tee with Shorts and Pants Set', 'Fashion', 39.99, 'Shorts and pants'),
('MU Green Sports Shorts and Joggers Set', 'Fashion', 39.99, 'Shorts and pants'),
('MU Blue Hoodie Pajama Shorts and Pants Set', 'Fashion', 39.99, 'Shorts and pants'),
('MU Red Plaid Pajama Tee and Bottoms Set', 'Fashion', 39.99, 'Shorts and pants'),
('MU Black Long Sleeve Pajama Shorts and Pants Set', 'Fashion', 39.99, 'Shorts and pants'),
('MU Classic Gray Button-Up Pajama Set', 'Fashion', 39.99, 'Shorts and pants');

INSERT INTO products (name, category, price, description) VALUES
('MU White Performance Socks Combo', 'Fashion', 14.99, 'Socks & Underwear'),
('MU Classic White & Black Socks Set', 'Fashion', 14.99, 'Socks & Underwear'),
('MU Multicolor Training Socks Pack', 'Fashion', 14.99, 'Socks & Underwear'),
('MU Red & Black Cushioned Socks Pack', 'Fashion', 14.99, 'Socks & Underwear'),
('MU Gray and White Everyday Socks Set', 'Fashion', 14.99, 'Socks & Underwear'),
('MU Red-Black-White Performance Sock Set', 'Fashion', 14.99, 'Socks & Underwear'),
('MU Logo Socks and Boxer Briefs Pack', 'Fashion', 19.99, 'Socks & Underwear'),
('MU Premium Black Underwear Pack', 'Fashion', 19.99, 'Socks & Underwear'),
('MU Striped Detail Boxer and Socks Combo', 'Fashion', 19.99, 'Socks & Underwear'),
('MU Training Socks & Underwear Essentials Set', 'Fashion', 19.99, 'Socks & Underwear');


INSERT INTO products (name, category, price, description) VALUES
('MU Dual-Look Long Coat Set', 'Fashion', 89.99, 'Jackets & Coats'),
('MU Dual-Look Parka Combo', 'Fashion', 89.99, 'Jackets & Coats'),
('MU Contrast Sleeve Hooded Set', 'Fashion', 89.99, 'Jackets & Coats'),
('MU Light Down Combo', 'Fashion', 89.99, 'Jackets & Coats'),
('MU Blue Pattern Jacket Set', 'Fashion', 89.99, 'Jackets & Coats'),
('MU Black Overhead Windbreaker Set', 'Fashion', 89.99, 'Jackets & Coats'),
('MU Red Zip-Up Jacket Set', 'Fashion', 89.99, 'Jackets & Coats'),
('MU Sporty Black Jacket Combo', 'Fashion', 89.99, 'Jackets & Coats'),
('MU Casual Zip Hoodie Pair', 'Fashion', 89.99, 'Jackets & Coats'),
('MU Green Activewear Set', 'Fashion', 89.99, 'Jackets & Coats'),
('MU Sage Green Outdoor Combo', 'Fashion', 89.99, 'Jackets & Coats'),
('MU Dual-Tone Training Jacket Set', 'Fashion', 89.99, 'Jackets & Coats'),
('MU Black Insulated Coat Set', 'Fashion', 89.99, 'Jackets & Coats'),
('MU Red Training Jacket Pair', 'Fashion', 89.99, 'Jackets & Coats'),
('MU Padded Vest Combo', 'Fashion', 89.99, 'Jackets & Coats'),
('MU Sporty Black Zip Jacket Set', 'Fashion', 89.99, 'Jackets & Coats'),
('MU Green Raincoat Pair', 'Fashion', 89.99, 'Jackets & Coats'),
('MU Blue Logo Sweatshirt Set', 'Fashion', 89.99, 'Jackets & Coats'),
('MU Black Winter Coat Set', 'Fashion', 89.99, 'Jackets & Coats'),
('MU Gray Casual Hoodie Set', 'Fashion', 89.99, 'Jackets & Coats'),
('MU Black Wind Jacket Pair', 'Fashion', 89.99, 'Jackets & Coats'),
('MU Contrast Sleeve Bomber Set', 'Fashion', 89.99, 'Jackets & Coats'),
('MU Black Zip Hoodie Combo', 'Fashion', 89.99, 'Jackets & Coats'),
('MU Olive Utility Jacket Set', 'Fashion', 89.99, 'Jackets & Coats');

INSERT INTO products (name, category, price, description) VALUES
('MU Black Minimalist Jacket Set', 'Fashion', 89.99, 'Jackets & Coats'),
('MU Street Zip Hoodie Duo', 'Fashion', 89.99, 'Jackets & Coats'),
('MU Dual Grey Puff Jacket Combo', 'Fashion', 89.99, 'Jackets & Coats'),
('MU White-Green Sport Jacket Pair', 'Fashion', 89.99, 'Jackets & Coats'),
('MU Green Track Jacket Combo', 'Fashion', 89.99, 'Jackets & Coats'),
('MU Blue Winter Coat Set', 'Fashion', 89.99, 'Jackets & Coats'),
('MU Red Bold Jacket Combo', 'Fashion', 89.99, 'Jackets & Coats'),
('MU Tactical Grey Zip Set', 'Fashion', 89.99, 'Jackets & Coats'),
('MU Black Sport Hoodie Duo', 'Fashion', 89.99, 'Jackets & Coats'),
('MU Navy Button-Up Combo', 'Fashion', 89.99, 'Jackets & Coats'),
('MU Deep Blue Raincoat Set', 'Fashion', 89.99, 'Jackets & Coats'),
('MU Sport Vest & Hoodie Pair', 'Fashion', 89.99, 'Jackets & Coats'),
('MU Slim Quilted Jacket Combo', 'Fashion', 89.99, 'Jackets & Coats'),
('MU Puffer Winter Duo', 'Fashion', 89.99, 'Jackets & Coats'),
('MU Black Street Hoodie Combo', 'Fashion', 89.99, 'Jackets & Coats'),
('MU Down Jacket with Stripes Set', 'Fashion', 89.99, 'Jackets & Coats'),
('MU Green Quilted Combo Jacket', 'Fashion', 89.99, 'Jackets & Coats'),
('MU Navy Fleece Jacket Duo', 'Fashion', 89.99, 'Jackets & Coats'),
('MU Red Zip Track Jacket Set', 'Fashion', 89.99, 'Jackets & Coats'),
('MU Red-Navy Contrast Hoodie Combo', 'Fashion', 89.99, 'Jackets & Coats'),
('MU Long Black Zip Jacket Duo', 'Fashion', 89.99, 'Jackets & Coats'),
('MU Winter Coat with Patch Logo Set', 'Fashion', 89.99, 'Jackets & Coats'),
('MU Classic Hooded Fur Jacket Combo', 'Fashion', 89.99, 'Jackets & Coats'),
('MU Wide Hemmed Fur Coat Pair', 'Fashion', 89.99, 'Jackets & Coats');
INSERT INTO products (name, category, price, description) VALUES
('MU White Retro Sneakers with Blue Stripes', 'Fashion', 69.99, 'Shoes'),
('MU Red-Black Soft Indoor Slippers', 'Fashion', 39.99, 'Shoes'),
('MU Black Logo Cozy Slippers', 'Fashion', 39.99, 'Shoes'),
('MU Classic Beige Sneakers with Red Stripes', 'Fashion', 69.99, 'Shoes');


INSERT INTO products (name, category, price, description) VALUES
('MU Round Badge Keychain Set', 'Souvenir', 9.99, 'Keychain'),
('MU ID & Jersey Mini Keychain Combo', 'Souvenir', 11.99, 'Keychain'),
('MU Round Crest Keychain Duo', 'Souvenir', 9.99, 'Keychain'),
('MU White Logo & Gold Shield Keychain Set', 'Souvenir', 12.99, 'Keychain'),
('MU Golden Crest Keychain Pair', 'Souvenir', 13.99, 'Keychain'),
('MU Red Tassel Keychain Combo', 'Souvenir', 10.99, 'Keychain'),
('MU Red & Black Loop Keychain Pair', 'Souvenir', 9.99, 'Keychain'),
('MU Spiral Strap Keychain Duo', 'Souvenir', 8.99, 'Keychain');  
INSERT INTO products (name, category, price, description) VALUES
('Adidas Compact Crossbody Bag', 'Souvenir', 19.99, 'Bags'),
('Black Classic Travel Duffel', 'Souvenir', 29.99, 'Bags'),
('Black Slim Carry-On Duffel', 'Souvenir', 27.99, 'Bags'),
('Red-Black Contrast Gym Bag', 'Souvenir', 26.99, 'Bags'),
('Red Sports Duffel with Handles', 'Souvenir', 28.99, 'Bags'),
('Slim Vertical Black Backpack', 'Souvenir', 24.99, 'Bags'),
('Sleek Urban Backpack with Strap', 'Souvenir', 23.99, 'Bags'),
('Plain Red Backpack', 'Souvenir', 21.99, 'Bags'),
('Manchester United Logo Backpack', 'Souvenir', 24.99, 'Bags'),
('MU Crest Lunch Cooler Bag', 'Souvenir', 18.99, 'Bags'),
('MU Crest Wallet Set', 'Souvenir', 16.99, 'Bags'),
('Compact Black Wallet Set', 'Souvenir', 14.99, 'Bags'),
('MU Crest Pencil Case Set', 'Souvenir', 13.99, 'Bags'),
('Blue-Red Crest Pouch Set', 'Souvenir', 13.99, 'Bags'),
('MU Crest Tote Bag – Black', 'Souvenir', 17.99, 'Bags'),
('MU Crest Tote Bag – White', 'Souvenir', 17.99, 'Bags'),
('Vertical Striped Shoulder Bag', 'Souvenir', 18.99, 'Bags'),
('Brown Canvas Shoulder Bag', 'Souvenir', 18.99, 'Bags'),
('Light Gray College Backpack', 'Souvenir', 25.99, 'Bags'),
('Matte Gray Tech Backpack', 'Souvenir', 26.99, 'Bags'),
('Navy School Backpack', 'Souvenir', 22.99, 'Bags'),
('Adidas Navy MU Backpack', 'Souvenir', 25.99, 'Bags'),
('Plain Red Sports Backpack', 'Souvenir', 23.99, 'Bags'),
('Red Crest Adidas Backpack', 'Souvenir', 26.99, 'Bags');

INSERT INTO products (name, category, price, description) VALUES
('Adidas Classic Black & White Mini Ball', 'Souvenir', 14.99, 'Balls'),
('Adidas MU Logo Panel Ball', 'Souvenir', 15.99, 'Balls'),
('Red Prestige Training Ball', 'Souvenir', 16.99, 'Balls'),
('MU Red Crest Panel Ball', 'Souvenir', 17.99, 'Balls'),
('White Curve Panel Training Ball', 'Souvenir', 16.49, 'Balls'),
('White Spiral MU Graphic Ball', 'Souvenir', 17.49, 'Balls'),
('Vintage Brown Leather Ball', 'Souvenir', 18.99, 'Balls'),
('Heritage Brown Gold Print Ball', 'Souvenir', 19.49, 'Balls'),
('Navy MU Signature Ball', 'Souvenir', 15.49, 'Balls'),
('Midnight Crest Adidas Ball', 'Souvenir', 16.49, 'Balls');


INSERT INTO products (name, category, price, description) VALUES
('MU Mascot Sport Bottle Set', 'Souvenir', 13.99, 'Bottles'),
('MU Mini Tumbler & Scarf Bottle Combo', 'Souvenir', 14.99, 'Bottles'),
('Classic MU Crest Stripe Bottle Duo', 'Souvenir', 15.49, 'Bottles'),
('MU Gold Logo Matte Black Bottle Set', 'Souvenir', 16.49, 'Bottles'),
('Red & Silver Gradient Travel Bottles', 'Souvenir', 14.99, 'Bottles'),
('Premium MU Steel Black Bottle Duo', 'Souvenir', 16.99, 'Bottles'),
('MU Glass Emblem Bottle Set', 'Souvenir', 13.49, 'Bottles'),
('MU Signature Red Stainless Bottle Pair', 'Souvenir', 15.99, 'Bottles'),
('MU Gradient Logo Steel Bottle Duo', 'Souvenir', 14.49, 'Bottles'),
('MU Thermo Red Bottle Combo', 'Souvenir', 15.99, 'Bottles'),
('MU Matte Black Lid Flask Pair', 'Souvenir', 13.99, 'Bottles'),
('MU Logo Round Handle Thermos Duo', 'Souvenir', 14.99, 'Bottles'),
('MU Hydration Lock Bottle Set', 'Souvenir', 13.49, 'Bottles');
 
