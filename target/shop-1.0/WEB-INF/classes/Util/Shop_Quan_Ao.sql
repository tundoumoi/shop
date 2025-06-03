USE Shop_Quan_Ao;
GO

-- 1. USERS
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY IDENTITY,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(100) ,
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
go

-- 11. REPORTING VIEWS
CREATE VIEW vw_MonthlyRevenue AS
SELECT
    YEAR(order_date) AS [Year],
    MONTH(order_date) AS [Month],
    SUM(total_amount) AS Revenue
FROM orders
WHERE payment_status = 'Paid'
GROUP BY YEAR(order_date), MONTH(order_date);
GO

