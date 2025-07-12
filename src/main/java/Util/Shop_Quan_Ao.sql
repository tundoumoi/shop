create DATABASE  Shop_Quan_Ao; 
USE Shop_Quan_Ao;
GO
UPDATE products
SET price = price * 1000;

-- 1. USERS
CREATE TABLE users (
    id INT PRIMARY KEY IDENTITY,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    full_name NVARCHAR(100),
    role VARCHAR(20) DEFAULT 'user',
    facebook_id VARCHAR(100) NULL,
    google_id   VARCHAR(100) NULL,
    address       NVARCHAR(500) NULL,
	status BIT NOT NULL DEFAULT 1,
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
    view_time  DECIMAL(10,2) NOT NULL DEFAULT 0
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

INSERT INTO product_images(product_id, image_url, is_primary)
VALUES 
    (1, 'images/jerseys/Home/man/man02 (1).jpg', 1), 
    (1, 'images/jerseys/Home/man/man01 (1).jpg', 0),
    (2, 'images/jerseys/Home/man/man04 (1).jpg', 1), 
    (2, 'images/jerseys/Home/man/man03 (1).jpg', 0),
    (3, 'images/jerseys/Home/man/man09 (1).jpg', 1), 
    (3, 'images/jerseys/Home/man/man10 (1).jpg', 0),
    (4, 'images/jerseys/Home/man/man05 (1).jpg', 1), 
    (4, 'images/jerseys/Home/man/man12 (1).jpg', 0),
    (5, 'images/jerseys/Home/man/man02.jpg', 1), 
    (5, 'images/jerseys/Home/man/man01.jpg', 0),
    (6, 'images/jerseys/Home/man/man14.jpg', 1), 
    (6, 'images/jerseys/Home/man/man15.jpg', 0),
    (7, 'images/jerseys/Home/man/man05.jpg', 1), 
    (7, 'images/jerseys/Home/man/man06.jpg', 0),
    (8, 'images/jerseys/Home/man/man07.jpg', 1), 
    (8, 'images/jerseys/Home/man/man06.jpg', 0),
    (9, 'images/jerseys/Home/man/man08.jpg', 1), 
    (9, 'images/jerseys/Home/man/man06.jpg', 0),
    (10, 'images/jerseys/Home/man/man09.jpg', 1), 
    (10, 'images/jerseys/Home/man/man06.jpg', 0),
    (11, 'images/jerseys/Home/man/man10.jpg', 1), 
    (11, 'images/jerseys/Home/man/man06.jpg', 0),
    (12, 'images/jerseys/Home/man/man11.jpeg', 1), 
    (12, 'images/jerseys/Home/man/man06.jpg', 0),
    (13, 'images/jerseys/Home/man/man16.jpg', 1), 
    (13, 'images/jerseys/Home/man/man06.jpg', 0),
    (14, 'images/jerseys/Home/man/amad.jpeg', 1), 
    (14, 'images/jerseys/Home/man/man06.jpg', 0),
    (15, 'images/jerseys/Home/man/man18.jpg', 1), 
    (15, 'images/jerseys/Home/man/man06.jpg', 0),
    (16, 'images/jerseys/Home/man/man19.jpg', 1), 
    (16, 'images/jerseys/Home/man/man06.jpg', 0),
    (17, 'images/jerseys/Home/man/man20.jpg', 1), 
    (17, 'images/jerseys/Home/man/man06.jpg', 0),
    (18, 'images/jerseys/away/manaway/awaymm10.jpg', 1),
    (18, 'images/jerseys/away/manaway/awaymm09.jpg', 0),
    (19, 'images/jerseys/away/manaway/awaymm03.jpg', 1),
    (19, 'images/jerseys/away/manaway/awaymm09.jpg', 0),
    (20, 'images/jerseys/away/manaway/awaymm05.jpg', 1),
    (20, 'images/jerseys/away/manaway/awaymm07.jpg', 0),
    (21, 'images/jerseys/away/manaway/awaymm06.jpg', 1),
    (21, 'images/jerseys/away/manaway/awaymm07.jpg', 0),
    (22, 'images/jerseys/away/manaway/awaym01.jpg', 1),
    (22, 'images/jerseys/away/manaway/awaym02.jpg', 0),
    (23, 'images/jerseys/away/manaway/awaym03.jpg', 1),
    (23, 'images/jerseys/away/manaway/awaym04.jpg', 0),
    (24, 'images/jerseys/away/manaway/awaym11.jpg', 1),
    (24, 'images/jerseys/away/manaway/awaym06.jpg', 0),
    (25, 'images/jerseys/away/manaway/awaym13.jpg', 1),
    (25, 'images/jerseys/away/manaway/awaym06.jpg', 0),
    (26, 'images/jerseys/away/manaway/awaym12.jpg', 1),
    (26, 'images/jerseys/away/manaway/awaymm06.jpg', 0),
    (27, 'images/jerseys/away/manaway/awaym10.jpg', 1),
    (27, 'images/jerseys/away/manaway/awaymm06.jpg', 0),
    (28, 'images/jerseys/away/manaway/awaym14.jpg', 1),
    (28, 'images/jerseys/away/manaway/awaym06.jpg', 0),
    (29, 'images/jerseys/away/manaway/awaymm01.jpg', 1),
    (29, 'images/jerseys/away/manaway/awaym06.jpg', 0),
    (30, 'images/jerseys/away/manaway/awaym07.jpg', 1),
    (30, 'images/jerseys/away/manaway/awaym06.jpg', 0),
    (31, 'images/jerseys/away/manaway/awaym05.jpg', 1),
    (31, 'images/jerseys/away/manaway/awaym06.jpg', 0),
    (32, 'images/jerseys/away/manaway/awaym14.jpg', 1),
    (32, 'images/jerseys/away/manaway/awaym06.jpg', 0),
    (33, 'images/jerseys/away/manaway/awaym09.jpg', 1),
    (33, 'images/jerseys/away/manaway/awaym06.jpg', 0),
    (34, 'images/jerseys/away/manaway/awaym08.jpg', 1),
    (34, 'images/jerseys/away/manaway/awaym06.jpg', 0);
    UPDATE product_images
SET is_primary = CASE
    WHEN is_primary = 1 THEN 0
    WHEN is_primary = 0 THEN 1
END
WHERE product_id = 41;

INSERT INTO product_images(product_id, image_url, is_primary)
VALUES 
-- Các dòng còn giữ nguyên
( 35, 'images/jerseys/Thirdd/manT/a06.jpg', 1 ),
( 35, 'images/jerseys/Thirdd/manT/a07.jpg', 0 ),
( 36, 'images/jerseys/Thirdd/manT/a09.jpg', 1 ),
( 36, 'images/jerseys/Thirdd/manT/a08.jpg', 0 ),
( 37, 'images/jerseys/Thirdd/manT/a10.jpg', 1 ),
( 37, 'images/jerseys/Thirdd/manT/a11.jpg', 0 ),
( 38, 'images/jerseys/Thirdd/manT/a05.jpg', 1 ),
( 38, 'images/jerseys/Thirdd/manT/a04.jpg', 0 ),
( 39, 'images/jerseys/Thirdd/manT/a01 (1).jpg', 1 ),
( 39, 'images/jerseys/Thirdd/manT/a02 (1).jpg', 0 ),
( 40, 'images/jerseys/Thirdd/manT/a12.jpg', 1 ),
( 40, 'images/jerseys/Thirdd/manT/a13.jpg', 0 ),
( 41, 'images/jerseys/Thirdd/manT/a01.jpg', 1 ),
( 41, 'images/jerseys/Thirdd/manT/a13 (1).jpg', 0 ),

-- Các dòng đã đổi thứ tự 0 <-> 1 từ id 42–51
( 42, 'images/jerseys/Thirdd/manT/a08 (1).jpg', 1 ),
( 42, 'images/jerseys/Thirdd/manT/a01.jpg', 0 ),
( 43, 'images/jerseys/Thirdd/manT/a14.jpg', 1 ),
( 43, 'images/jerseys/Thirdd/manT/a01.jpg', 0 ),
( 44, 'images/jerseys/Thirdd/manT/a09 (1).jpg', 1 ),
( 44, 'images/jerseys/Thirdd/manT/a01.jpg', 0 ),
( 45, 'images/jerseys/Thirdd/manT/a11 (1).jpg', 1 ),
( 45, 'images/jerseys/Thirdd/manT/a01.jpg', 0 ),
( 46, 'images/jerseys/Thirdd/manT/a12 (1).jpg', 1 ),
( 46, 'images/jerseys/Thirdd/manT/a01.jpg', 0 ),
( 47, 'images/jerseys/Thirdd/manT/a03 (1).jpg', 1 ),
( 47, 'images/jerseys/Thirdd/manT/a01.jpg', 0 ),
( 48, 'images/jerseys/Thirdd/manT/a04 (1).jpg', 1 ),
( 48, 'images/jerseys/Thirdd/manT/a01.jpg', 0 ),
( 49, 'images/jerseys/Thirdd/manT/a15.jpg', 1 ),
( 49, 'images/jerseys/Thirdd/manT/a01.jpg', 0 ),
( 50, 'images/jerseys/Thirdd/manT/a06 (1).jpg', 1 ),
( 50, 'images/jerseys/Thirdd/manT/a01.jpg', 0 ),
( 51, 'images/jerseys/Thirdd/manT/a10 (1).jpg', 1 ),
( 51, 'images/jerseys/Thirdd/manT/a01.jpg', 0 );


INSERT INTO product_images(product_id, image_url, is_primary)
VALUES 
( 52, 'images/jerseys/GK/degea.jpg', 1 ),
( 52, 'images/jerseys/GK/a01.png', 0 ),
( 53, 'images/jerseys/GK/a3.jpg', 1 ),
( 53, 'images/jerseys/GK/a4.jpg', 0 ),
( 54, 'images/jerseys/GK/a7.jpg', 1 ),
( 54, 'images/jerseys/GK/a8.jpg', 0 ),
( 55, 'images/jerseys/GK/a5.jpg', 1 ),
( 55, 'images/jerseys/GK/a6.jpg', 0 ),
( 56, 'images/jerseys/GK/daiden.jpg', 1 ),
( 56, 'images/jerseys/GK/daiden1.jpg', 0 ),
( 57, 'images/jerseys/GK/tatden.jpg', 1 ),
( 57, 'images/jerseys/GK/tatden1.jpg', 0 ),
( 58, 'images/jerseys/GK/a1.jpg', 1 ),
( 58, 'images/jerseys/GK/a2.jpg', 0 );


DELETE FROM product_images
WHERE product_id BETWEEN 80 AND 89;
INSERT INTO product_images(product_id, image_url, is_primary)
VALUES 
(59, 'images/training/áo01.jpg', 1),
(59, 'images/training/áo02.jpg', 0),
(60, 'images/training/áo03.jpg', 1),
(60, 'images/training/áo04.jpg', 0),
(61, 'images/training/áo07.jpg', 1),
(61, 'images/training/áo08.jpg', 0),
(62, 'images/training/áo09.jpg', 1),
(62, 'images/training/áo10.jpg', 0),
(63, 'images/training/áo11.jpg', 1),
(63, 'images/training/áo12.jpg', 0),
(64, 'images/training/áo13.jpg', 1),
(64, 'images/training/áo14.jpg', 0),
(65, 'images/training/áo15.jpg', 1),
(65, 'images/training/áo16.jpg', 0),
(66, 'images/training/áo17.jpg', 1),
(66, 'images/training/áo18.jpg', 0),
(67, 'images/training/áo19.jpg', 1),
(67, 'images/training/áo20.jpg', 0),
(68, 'images/training/áo21.jpg', 1),
(68, 'images/training/áo22.jpg', 0),
(69, 'images/training/áo23.jpg', 1),
(69, 'images/training/áo24.jpg', 0),
(70, 'images/training/áo29.jpg', 1),
(70, 'images/training/áo30.jpg', 0),
(71, 'images/training/áo31.jpg', 1),
(71, 'images/training/áo32.jpg', 0),
(72, 'images/training/áo33.jpg', 1),
(72, 'images/training/áo34.jpg', 0),
(73, 'images/training/áo35.jpg', 1),
(73, 'images/training/áo36.jpg', 0),
(74, 'images/training/áo37.jpg', 1),
(74, 'images/training/áo38.jpg', 0),
(75, 'images/training/áo39.jpg', 1),
(75, 'images/training/áo40.jpg', 0);
INSERT INTO product_images(product_id, image_url, is_primary)
VALUES 
(76, 'images/training/áo29.jpg', 1),
(76, 'images/training/áo30.jpg', 0),
(77, 'images/training/áo31.jpg', 1),
(77, 'images/training/áo33.jpg', 0);
INSERT INTO product_images(product_id, image_url, is_primary)
VALUES 
(78, 'images/training/41.jpg', 1),
(78, 'images/training/42.jpg', 0),
(79, 'images/training/43.jpg', 1),
(79, 'images/training/44.jpg', 0);
INSERT INTO product_images(product_id, image_url, is_primary)
VALUES 
(80, 'images/training/11.jpg', 1),
(80, 'images/training/12.jpg', 0);
INSERT INTO product_images(product_id, image_url, is_primary)
VALUES 

(81, 'images/training/13.jpg', 1),
(81, 'images/training/14.jpg', 0),
(82, 'images/training/15.jpg', 1),
(82, 'images/training/16.jpg', 0),
(83, 'images/training/17.jpg', 1),
(83, 'images/training/18.jpg', 0),
(84, 'images/training/19.jpg', 1),
(84, 'images/training/20.jpg', 0),
(85, 'images/training/21.jpg', 1),
(85, 'images/training/22.jpg', 0),
(86, 'images/training/23.jpg', 1),
(86, 'images/training/24.jpg', 0),
(87, 'images/training/25.jpg', 1),
(87, 'images/training/26.jpg', 0),
(88, 'images/training/27.jpg', 1),
(88, 'images/training/28.jpg', 0),
(89, 'images/training/29.jpg', 1),
(89, 'images/training/30.jpg', 0);


INSERT INTO product_images(product_id, image_url, is_primary)
VALUES
(90, 'images/afashion/Tshirt/ao01.jpg', 1),
(90, 'images/afashion/Tshirt/ao02.jpg', 0),
(91, 'images/afashion/Tshirt/ao03.jpg', 1),
(91, 'images/afashion/Tshirt/ao04.jpg', 0),
(92, 'images/afashion/Tshirt/ao05.jpg', 1),
(92, 'images/afashion/Tshirt/ao06.jpg', 0),
(93, 'images/afashion/Tshirt/ao07.jpg', 1),
(93, 'images/afashion/Tshirt/ao08.jpg', 0),
(94, 'images/afashion/Tshirt/ao09.jpg', 1),
(94, 'images/afashion/Tshirt/ao10.jpg', 0),
(95, 'images/afashion/Tshirt/ao11.jpg', 1),
(95, 'images/afashion/Tshirt/ao12.jpg', 0),
(96, 'images/afashion/Tshirt/ao13.jpg', 1),
(96, 'images/afashion/Tshirt/ao14.jpg', 0),
(97, 'images/afashion/Tshirt/ao15.jpg', 1),
(97, 'images/afashion/Tshirt/ao16.jpg', 0),
(98, 'images/afashion/Tshirt/ao17.jpg', 1),
(98, 'images/afashion/Tshirt/ao18.jpg', 0),
(99, 'images/afashion/Tshirt/ao19.jpg', 1),
(99, 'images/afashion/Tshirt/ao20.jpg', 0),
(100, 'images/afashion/Tshirt/ao21.jpg', 1),
(100, 'images/afashion/Tshirt/ao22.jpg', 0),
(101, 'images/afashion/Tshirt/ao23.jpg', 1),
(101, 'images/afashion/Tshirt/ao24.jpg', 0),
(102, 'images/afashion/Tshirt/ao25.jpg', 1),
(102, 'images/afashion/Tshirt/ao26.jpg', 0),
(103, 'images/afashion/Tshirt/ao27.jpg', 1),
(103, 'images/afashion/Tshirt/ao28.jpg', 0),
(104, 'images/afashion/Tshirt/ao29.jpg', 1),
(104, 'images/afashion/Tshirt/ao30.jpg', 0),
(105, 'images/afashion/Tshirt/ao31.jpg', 1),
(105, 'images/afashion/Tshirt/ao32.jpg', 0),
(106, 'images/afashion/Tshirt/ao33.jpg', 1),
(106, 'images/afashion/Tshirt/ao34.jpg', 0),
(107, 'images/afashion/Tshirt/ao35.jpg', 1),
(107, 'images/afashion/Tshirt/ao36.jpg', 0),
(108, 'images/afashion/Tshirt/ao37.jpg', 1),
(108, 'images/afashion/Tshirt/ao38.jpg', 0),
(109, 'images/afashion/Tshirt/ao39.jpg', 1),
(109, 'images/afashion/Tshirt/ao40.jpg', 0),
(110, 'images/afashion/Tshirt/ao41.jpg', 1),
(110, 'images/afashion/Tshirt/ao42.jpg', 0),
(111, 'images/afashion/Tshirt/ao43.jpg', 1),
(111, 'images/afashion/Tshirt/ao44.jpg', 0);

INSERT INTO product_images(product_id, image_url, is_primary)
VALUES
(112, 'images/afashion/Tshirt/ao01 (1).jpg', 1),
(112, 'images/afashion/Tshirt/ao02 (1).jpg', 0),
(113, 'images/afashion/Tshirt/ao03 (1).jpg', 1),
(113, 'images/afashion/Tshirt/ao04 (1).jpg', 0),
(114, 'images/afashion/Tshirt/ao05 (1).jpg', 1),
(114, 'images/afashion/Tshirt/ao06 (1).jpg', 0),
(115, 'images/afashion/Tshirt/ao07 (1).jpg', 1),
(115, 'images/afashion/Tshirt/ao08 (1).jpg', 0),
(116, 'images/afashion/Tshirt/ao09 (1).jpg', 1),
(116, 'images/afashion/Tshirt/ao10 (1).jpg', 0),
(117, 'images/afashion/Tshirt/ao11 (1).jpg', 1),
(117, 'images/afashion/Tshirt/ao12 (1).jpg', 0),
(118, 'images/afashion/Tshirt/ao13 (1).jpg', 1),
(118, 'images/afashion/Tshirt/ao14 (1).jpg', 0),
(119, 'images/afashion/Tshirt/ao15 (1).jpg', 1),
(119, 'images/afashion/Tshirt/ao16 (1).jpg', 0),
(120, 'images/afashion/Tshirt/ao17 (1).jpg', 1),
(120, 'images/afashion/Tshirt/ao18 (1).jpg', 0),
(121, 'images/afashion/Tshirt/ao19 (1).jpg', 1),
(121, 'images/afashion/Tshirt/ao20 (1).jpg', 0),
(122, 'images/afashion/Tshirt/ao21 (1).jpg', 1),
(122, 'images/afashion/Tshirt/ao22 (1).jpg', 0),
(123, 'images/afashion/Tshirt/ao23 (1).jpg', 1),
(123, 'images/afashion/Tshirt/ao24 (1).jpg', 0),
(124, 'images/afashion/Tshirt/ao25 (1).jpg', 1),
(124, 'images/afashion/Tshirt/ao26 (1).jpg', 0),
(125, 'images/afashion/Tshirt/ao27 (1).jpg', 1),
(125, 'images/afashion/Tshirt/ao28 (1).jpg', 0),
(126, 'images/afashion/Tshirt/ao29 (1).jpg', 1),
(126, 'images/afashion/Tshirt/ao30 (1).jpg', 0),
(127, 'images/afashion/Tshirt/ao31 (1).jpg', 1),
(127, 'images/afashion/Tshirt/ao32 (1).jpg', 0),
(128, 'images/afashion/Tshirt/ao33 (1).jpg', 1),
(128, 'images/afashion/Tshirt/ao34 (1).jpg', 0),
(129, 'images/afashion/Tshirt/ao35 (1).jpg', 1),
(129, 'images/afashion/Tshirt/ao36 (1).jpg', 0),
(130, 'images/afashion/Tshirt/ao37 (1).jpg', 1),
(130, 'images/afashion/Tshirt/ao38 (1).jpg', 0),
(131, 'images/afashion/Tshirt/ao39 (1).jpg', 1),
(131, 'images/afashion/Tshirt/ao40 (1).jpg', 0),
(132, 'images/afashion/Tshirt/ao41 (1).jpg', 1),
(132, 'images/afashion/Tshirt/ao42 (1).jpg', 0),
(133, 'images/afashion/Tshirt/ao43 (1).jpg', 1),
(133, 'images/afashion/Tshirt/ao44 (1).jpg', 0),
(134, 'images/afashion/Tshirt/ao45.jpg', 1),
(134, 'images/afashion/Tshirt/ao46.jpg', 0),
(135, 'images/afashion/Tshirt/ao001.jpg', 1),
(135, 'images/afashion/Tshirt/ao002.jpg', 0),
(136, 'images/afashion/Tshirt/ao003.jpg', 1),
(136, 'images/afashion/Tshirt/ao004.jpg', 0);

INSERT INTO product_images(product_id, image_url, is_primary)
VALUES
(137, 'images/afashion/hoodie/h01.jpg', 1),
(137, 'images/afashion/hoodie/h02.jpg', 0),
(138, 'images/afashion/hoodie/h03.jpg', 1),
(138, 'images/afashion/hoodie/h04.jpg', 0),
(139, 'images/afashion/hoodie/h05.jpg', 1),
(139, 'images/afashion/hoodie/h06.jpg', 0),
(140, 'images/afashion/hoodie/h07.jpg', 1),
(140, 'images/afashion/hoodie/h08.jpg', 0),
(141, 'images/afashion/hoodie/h09.jpg', 1),
(141, 'images/afashion/hoodie/h10.jpg', 0),
(142, 'images/afashion/hoodie/h11.jpg', 1),
(142, 'images/afashion/hoodie/h12.jpg', 0),
(143, 'images/afashion/hoodie/h13.jpg', 1),
(143, 'images/afashion/hoodie/h14.jpg', 0),
(144, 'images/afashion/hoodie/h15.jpg', 1),
(144, 'images/afashion/hoodie/h16.jpg', 0),
(145, 'images/afashion/hoodie/h17.jpg', 1),
(145, 'images/afashion/hoodie/h18.jpg', 0),
(146, 'images/afashion/hoodie/h19.jpg', 1),
(146, 'images/afashion/hoodie/h20.jpg', 0),
(147, 'images/afashion/hoodie/h21.jpg', 1),
(147, 'images/afashion/hoodie/h22.jpg', 0),
(148, 'images/afashion/hoodie/h23.jpg', 1),
(148, 'images/afashion/hoodie/h24.jpg', 0),
(149, 'images/afashion/hoodie/h25.jpg', 1),
(149, 'images/afashion/hoodie/h26.jpg', 0),
(150, 'images/afashion/hoodie/h27.jpg', 1),
(150, 'images/afashion/hoodie/h28.jpg', 0),
(151, 'images/afashion/hoodie/h29.jpg', 1),
(151, 'images/afashion/hoodie/h30.jpg', 0),
(152, 'images/afashion/hoodie/h31.jpg', 1),
(152, 'images/afashion/hoodie/h32.jpg', 0),
(153, 'images/afashion/hoodie/h33.jpg', 1),
(153, 'images/afashion/hoodie/h34.jpg', 0),
(154, 'images/afashion/hoodie/h35.jpg', 1),
(154, 'images/afashion/hoodie/h36.jpg', 0),
(155, 'images/afashion/hoodie/h37.jpg', 1),
(155, 'images/afashion/hoodie/h38.jpg', 0),
(156, 'images/afashion/hoodie/h39.jpg', 1),
(156, 'images/afashion/hoodie/h40.jpg', 0),
(157, 'images/afashion/hoodie/h41.jpg', 1),
(157, 'images/afashion/hoodie/h42.jpg', 0),
(158, 'images/afashion/hoodie/h43.jpg', 1),
(158, 'images/afashion/hoodie/h44.jpg', 0),
(159, 'images/afashion/hoodie/h45.jpg', 1),
(159, 'images/afashion/hoodie/h46.jpg', 0),
(160, 'images/afashion/hoodie/h47.jpg', 1),
(160, 'images/afashion/hoodie/h48.jpg', 0),
(161, 'images/afashion/hoodie/hh01.jpg', 1),
(161, 'images/afashion/hoodie/hh02.jpg', 0),
(162, 'images/afashion/hoodie/hh03.jpg', 1),
(162, 'images/afashion/hoodie/hh04.jpg', 0),
(163, 'images/afashion/hoodie/hh05.jpg', 1),
(163, 'images/afashion/hoodie/hh06.jpg', 0),
(164, 'images/afashion/hoodie/hh07.jpg', 1),
(164, 'images/afashion/hoodie/hh08.jpg', 0),
(165, 'images/afashion/hoodie/hh09.jpg', 1),
(165, 'images/afashion/hoodie/hh10.jpg', 0),
(166, 'images/afashion/hoodie/hh11.jpg', 1),
(166, 'images/afashion/hoodie/hh12.jpg', 0),
(167, 'images/afashion/hoodie/hh13.jpg', 1),
(167, 'images/afashion/hoodie/hh14.jpg', 0),
(168, 'images/afashion/hoodie/hh15.jpg', 1),
(168, 'images/afashion/hoodie/hh16.jpg', 0),
(169, 'images/afashion/hoodie/hh17.jpg', 1),
(169, 'images/afashion/hoodie/hh18.jpg', 0),
(170, 'images/afashion/hoodie/hh19.jpg', 1),
(170, 'images/afashion/hoodie/hh20.jpg', 0),
(171, 'images/afashion/hoodie/hh21.jpg', 1),
(171, 'images/afashion/hoodie/hh22.jpg', 0),
(172, 'images/afashion/hoodie/hh23.jpg', 1),
(172, 'images/afashion/hoodie/hh24.jpg', 0),
(173, 'images/afashion/hoodie/hh25.jpg', 1),
(173, 'images/afashion/hoodie/hh26.jpg', 0),
(174, 'images/afashion/hoodie/hh27.jpg', 1),
(174, 'images/afashion/hoodie/hh28.jpg', 0),
(175, 'images/afashion/hoodie/hh29.jpg', 1),
(175, 'images/afashion/hoodie/hh30.jpg', 0),
(176, 'images/afashion/hoodie/hh31.jpg', 1),
(176, 'images/afashion/hoodie/hh32.jpg', 0),
(177, 'images/afashion/hoodie/hh33.jpg', 1),
(177, 'images/afashion/hoodie/hh34.jpg', 0),
(178, 'images/afashion/hoodie/hh35.jpg', 1),
(178, 'images/afashion/hoodie/hh36.jpg', 0),
(179, 'images/afashion/hoodie/hh37.jpg', 1),
(179, 'images/afashion/hoodie/hh38.jpg', 0),
(180, 'images/afashion/hoodie/hh39.jpg', 1),
(180, 'images/afashion/hoodie/hh40.jpg', 0),
(181, 'images/afashion/hoodie/hh41.jpg', 1),
(181, 'images/afashion/hoodie/hh42.jpg', 0),
(182, 'images/afashion/hoodie/hh43.jpg', 1),
(182, 'images/afashion/hoodie/hh44.jpg', 0),
(183, 'images/afashion/hoodie/hh45.jpg', 1),
(183, 'images/afashion/hoodie/hh46.jpg', 0),
(184, 'images/afashion/hoodie/hh47.jpg', 1),
(184, 'images/afashion/hoodie/hh48.jpg', 0);

DELETE FROM product_images
WHERE product_id BETWEEN 80 AND 89;
INSERT INTO product_images(product_id, image_url, is_primary)
VALUES
(185, 'images/afashion/reto/reto01.jpg', 1),
(185, 'images/afashion/reto/reto02.jpg', 0),
(186, 'images/afashion/reto/reto03.jpg', 1),
(186, 'images/afashion/reto/reto04.jpg', 0),
(187, 'images/afashion/reto/reto05.jpg', 1),
(187, 'images/afashion/reto/reto06.jpg', 0),
(188, 'images/afashion/reto/reto07.jpg', 1),
(188, 'images/afashion/reto/reto08.jpg', 0),
(189, 'images/afashion/reto/reto09.jpg', 1),
(189, 'images/afashion/reto/reto10.jpg', 0);

DELETE FROM product_images
WHERE product_id BETWEEN 185 AND 189;
INSERT INTO product_images(product_id, image_url, is_primary)
VALUES
(190, 'images/afashion/reto/r1.jpg', 1),
(190, 'images/afashion/reto/r2.jpg', 0),
(191, 'images/afashion/reto/r3.jpg', 1),
(191, 'images/afashion/reto/r4.jpg', 0),
(192, 'images/afashion/reto/r5.jpg', 1),
(192, 'images/afashion/reto/r6.jpg', 0),
(193, 'images/afashion/reto/r7.jpg', 1),
(193, 'images/afashion/reto/r8.jpg', 0);
INSERT INTO product_images(product_id, image_url, is_primary)
VALUES
(194, '../images/afashion/reto/reto19.jpg', 1),
(194, '../images/afashion/reto/reto20.jpg', 0),
(195, '../images/afashion/reto/reto21.jpg', 1),
(195, '../images/afashion/reto/reto22.jpg', 0),
(196, '../images/afashion/reto/reto23.jpg', 1),
(196, '../images/afashion/reto/reto24.jpg', 0),
(197, '../images/afashion/reto/reto25.jpg', 1),
(197, '../images/afashion/reto/reto26.jpg', 0),
(198, '../images/afashion/reto/reto1.jpg', 1),
(198, '../images/afashion/reto/reto2.jpg', 0),
(199, '../images/afashion/reto/reto3.jpg', 1),
(199, '../images/afashion/reto/reto4.jpg', 0),
(200, '../images/afashion/reto/reto5.jpg', 1),
(200, '../images/afashion/reto/reto6.jpg', 0);

INSERT INTO product_images(product_id, image_url, is_primary)
VALUES
(201, '../images/afashion/nightware/1.jpg', 1),
(201, '../images/afashion/nightware/2.jpg', 0),
(202, '../images/afashion/nightware/3.jpg', 1),
(202, '../images/afashion/nightware/4.jpg', 0),
(203, '../images/afashion/nightware/5.jpg', 1),
(203, '../images/afashion/nightware/6.jpg', 0),
(204, '../images/afashion/nightware/7.jpg', 1),
(204, '../images/afashion/nightware/8.jpg', 0),
(205, '../images/afashion/nightware/9.jpg', 1),
(205, '../images/afashion/nightware/10.jpg', 0),
(206, '../images/afashion/nightware/11.jpg', 1),
(206, '../images/afashion/nightware/12.jpg', 0),
(207, '../images/afashion/nightware/13.jpg', 1),
(207, '../images/afashion/nightware/14.jpg', 0),
(208, '../images/afashion/nightware/15.jpg', 1),
(208, '../images/afashion/nightware/16.jpg', 0);
INSERT INTO product_images(product_id, image_url, is_primary)
VALUES
(209, '../images/afashion/short/quan01.jpg', 1),
(209, '../images/afashion/short/quan02.jpg', 0),
(210, '../images/afashion/short/quan03.jpg', 1),
(210, '../images/afashion/short/quan04.jpg', 0),
(211, '../images/afashion/short/quan05.jpg', 1),
(211, '../images/afashion/short/quan06.jpg', 0),
(212, '../images/afashion/short/quan07.jpg', 1),
(212, '../images/afashion/short/quan08.jpg', 0),
(213, '../images/afashion/short/quan09.jpg', 1),
(213, '../images/afashion/short/quan10.jpg', 0),
(214, '../images/afashion/short/quan11.jpg', 1),
(214, '../images/afashion/short/quan12.jpg', 0),
(215, '../images/afashion/short/quan13.jpg', 1),
(215, '../images/afashion/short/quan14.jpg', 0),
(216, '../images/afashion/short/quan15.jpg', 1),
(216, '../images/afashion/short/quan16.jpg', 0),
(217, '../images/afashion/short/quan17.jpg', 1),
(217, '../images/afashion/short/quan18.jpg', 0),
(218, '../images/afashion/short/quan19.jpg', 1),
(218, '../images/afashion/short/quan20.jpg', 0),
(219, '../images/afashion/short/quan21.jpg', 1),
(219, '../images/afashion/short/quan22.jpg', 0),
(220, '../images/afashion/short/quan23.jpg', 1),
(220, '../images/afashion/short/quan24.jpg', 0),
(221, '../images/afashion/short/quan25.jpg', 1),
(221, '../images/afashion/short/quan26.jpg', 0),
(222, '../images/afashion/short/quan27.jpg', 1),
(222, '../images/afashion/short/quan28.jpg', 0),
(223, '../images/afashion/short/quan29.jpg', 1),
(223, '../images/afashion/short/quan30.jpg', 0),
(224, '../images/afashion/short/quan31.jpg', 1),
(224, '../images/afashion/short/quan32.jpg', 0),
(225, '../images/afashion/short/quan33.jpg', 1),
(225, '../images/afashion/short/quan34.jpg', 0),
(226, '../images/afashion/short/quan35.jpg', 1),
(226, '../images/afashion/short/quan36.jpg', 0),
(227, '../images/afashion/short/quan37.jpg', 1),
(227, '../images/afashion/short/quan38.jpg', 0),
(228, '../images/afashion/short/quan39.jpg', 1),
(228, '../images/afashion/short/quan40.jpg', 0),
(229, '../images/afashion/short/quan41.jpg', 1),
(229, '../images/afashion/short/quan42.jpg', 0),
(230, '../images/afashion/short/quan43.jpg', 1),
(230, '../images/afashion/short/quan44.jpg', 0),
(231, '../images/afashion/short/quan45.jpg', 1),
(231, '../images/afashion/short/quan46.jpg', 0),
(232, '../images/afashion/short/quan47.jpg', 1),
(232, '../images/afashion/short/quan48.jpg', 0),
(233, '../images/afashion/short/quan1.jpg', 1),
(233, '../images/afashion/short/quan2.jpg', 0),
(234, '../images/afashion/short/quan3.jpg', 1),
(234, '../images/afashion/short/quan4.jpg', 0),
(235, '../images/afashion/short/quan5.jpg', 1),
(235, '../images/afashion/short/quan6.jpg', 0),
(236, '../images/afashion/short/quan7.jpg', 1),
(236, '../images/afashion/short/quan8.jpg', 0),
(237, '../images/afashion/short/quan49.jpg', 1),
(237, '../images/afashion/short/quan50.jpg', 0);
INSERT INTO product_images(product_id, image_url, is_primary)
VALUES
(238, '../images/afashion/stock&underware/1.jpg', 1),
(238, '../images/afashion/stock&underware/2.jpg', 0),

(239, '../images/afashion/stock&underware/3.jpg', 1),
(239, '../images/afashion/stock&underware/4.jpg', 0),

(240, '../images/afashion/stock&underware/5.jpg', 1),
(240, '../images/afashion/stock&underware/6.jpg', 0),

(241, '../images/afashion/stock&underware/7.jpg', 1),
(241, '../images/afashion/stock&underware/8.jpg', 0),

(242, '../images/afashion/stock&underware/9.jpg', 1),
(242, '../images/afashion/stock&underware/10.jpg', 0),

(243, '../images/afashion/stock&underware/11.jpg', 1),
(243, '../images/afashion/stock&underware/12.jpg', 0),

(244, '../images/afashion/stock&underware/13.jpg', 1),
(244, '../images/afashion/stock&underware/14.jpg', 0),

(245, '../images/afashion/stock&underware/15.jpg', 1),
(245, '../images/afashion/stock&underware/16.jpg', 0),

(246, '../images/afashion/stock&underware/17.jpg', 1),
(246, '../images/afashion/stock&underware/18.jpg', 0);
INSERT INTO product_images(product_id, image_url, is_primary)
VALUES
(247, '../images/afashion/footwear/1.jpg', 1),
(247, '../images/afashion/footwear/2.jpg', 0),

(248, '../images/afashion/footwear/3.jpg', 1),
(248, '../images/afashion/footwear/4.jpg', 0),

(249, '../images/afashion/footwear/5.jpg', 1),
(249, '../images/afashion/footwear/6.jpg', 0),

(250, '../images/afashion/footwear/7.jpg', 1),
(250, '../images/afashion/footwear/8.jpg', 0);
DELETE FROM product_images
WHERE product_id BETWEEN 251 AND 258;
INSERT INTO product_images(product_id, image_url, is_primary)
VALUES
(251, 'images/souvenirs/mockhoa/1.jpg', 1),
(251, 'images/souvenirs/mockhoa/2.jpg', 0),

(252, 'images/souvenirs/mockhoa/3.jpg', 1),
(252, 'images/souvenirs/mockhoa/4.jpg', 0),

(253, 'images/souvenirs/mockhoa/5.jpg', 1),
(253, 'images/souvenirs/mockhoa/6.jpg', 0),

(254, 'images/souvenirs/mockhoa/7.jpg', 1),
(254, 'images/souvenirs/mockhoa/8.jpg', 0),

(255, 'images/souvenirs/mockhoa/9.jpg', 1),
(255, 'images/souvenirs/mockhoa/10.jpg', 0),

(256, 'images/souvenirs/mockhoa/11.jpg', 1),
(256, 'images/souvenirs/mockhoa/12.jpg', 0),

(257, 'images/souvenirs/mockhoa/13.jpg', 1),
(257, 'images/souvenirs/mockhoa/14.jpg', 0),

(258, 'images/souvenirs/mockhoa/15.jpg', 1),
(258, 'images/souvenirs/mockhoa/16.jpg', 0);

INSERT INTO product_images(product_id, image_url, is_primary)
VALUES
(259, '../images/souvenirs/ball/1.jpg', 1),
(259, '../images/souvenirs/ball/2.jpg', 0),

(260, '../images/souvenirs/ball/3.jpg', 1),
(260, '../images/souvenirs/ball/4.jpg', 0),

(261, '../images/souvenirs/ball/5.jpg', 1),
(261, '../images/souvenirs/ball/6.jpg', 0),

(262, '../images/souvenirs/ball/7.jpg', 1),
(262, '../images/souvenirs/ball/8.jpg', 0),

(263, '../images/souvenirs/ball/9.jpg', 1),
(263, '../images/souvenirs/ball/10.jpg', 0),

(264, '../images/souvenirs/ball/11.jpg', 1),
(264, '../images/souvenirs/ball/12.jpg', 0),

(265, '../images/souvenirs/ball/13.jpg', 1),
(265, '../images/souvenirs/ball/14.jpg', 0),

(266, '../images/souvenirs/ball/15.jpg', 1),
(266, '../images/souvenirs/ball/16.jpg', 0),

(267, '../images/souvenirs/ball/17.jpg', 1),
(267, '../images/souvenirs/ball/18.jpg', 0),

(268, '../images/souvenirs/ball/19.jpg', 1),
(268, '../images/souvenirs/ball/20.jpg', 0);

INSERT INTO product_images(product_id, image_url, is_primary)
VALUES
(269, '../images/souvenirs/bottle/1.jpg', 1),
(269, '../images/souvenirs/bottle/2.jpg', 0),

(270, '../images/souvenirs/bottle/3.jpg', 1),
(270, '../images/souvenirs/bottle/4.jpg', 0),

(271, '../images/souvenirs/bottle/5.jpg', 1),
(271, '../images/souvenirs/bottle/6.jpg', 0),

(272, '../images/souvenirs/bottle/7.jpg', 1),
(272, '../images/souvenirs/bottle/8.jpg', 0),

(273, '../images/souvenirs/bottle/9.jpg', 1),
(273, '../images/souvenirs/bottle/10.jpg', 0),

(274, '../images/souvenirs/bottle/11.jpg', 1),
(274, '../images/souvenirs/bottle/12.jpg', 0),

(275, '../images/souvenirs/bottle/13.jpg', 1),
(275, '../images/souvenirs/bottle/14.jpg', 0),

(276, '../images/souvenirs/bottle/15.jpg', 1),
(276, '../images/souvenirs/bottle/16.jpg', 0),

(277, '../images/souvenirs/bottle/17.jpg', 1),
(277, '../images/souvenirs/bottle/18.jpg', 0),

(278, '../images/souvenirs/bottle/19.jpg', 1),
(278, '../images/souvenirs/bottle/20.jpg', 0),

(279, '../images/souvenirs/bottle/21.jpg', 1),
(279, '../images/souvenirs/bottle/22.jpg', 0),

(280, '../images/souvenirs/bottle/23.jpg', 1),
(280, '../images/souvenirs/bottle/24.jpg', 0),

(281, '../images/souvenirs/bottle/25.jpg', 1),
(281, '../images/souvenirs/bottle/26.jpg', 0);



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
('MU Training Socks & Underwear Essentials Set', 'Fashion', 19.99, 'Socks & Underwear');

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
 
go
 -- product_variants 
INSERT INTO product_variants (product_id, size, quantity)
SELECT p.id, s.size, 20
FROM products p
CROSS JOIN (VALUES ('S'), ('M'), ('L'), ('XL')) AS s(size);
go

-- 1) Thêm 5 người dùng mẫu
INSERT INTO users (email, password, full_name, role, address)
VALUES
  ('alice@example.com','$2y$10$xxxxx','Alice Nguyen','user','Hanoi, Vietnam'),
  ('bob@example.com','$2y$10$xxxxx','Bob Tran','user','Hanoi, Vietnam'),
  ('carol@example.com','$2y$10$xxxxx','Carol Le','user','Ho Chi Minh City, Vietnam'),
  ('david@example.com','$2y$10$xxxxx','David Pham','user','Da Nang, Vietnam'),
  ('eve@example.com','$2y$10$xxxxx','Eve Ho','user','Hue, Vietnam');

go
DELETE FROM product_images;
DELETE FROM product_views;
DELETE FROM order_items;
DELETE FROM orders;
DELETE FROM recommendations;

go 
INSERT INTO product_views (user_id, product_id, view_time) VALUES
  (1,  5,  2),   -- user 1 xem product #5 trong 2 phút
  (1, 12, 10),   -- user 1 xem product #12 trong 10 phút
  (1,  8, 15),   -- user 1 xem product #8 trong 15 phút
  (1, 12,  7),   -- xem lại product #12 trong 7 phút
  (1, 20,  5);   -- user 1 xem product #20 trong 5 phút  

go

-- 1) Gom tất cả category hiện có
SELECT DISTINCT category
INTO   #AllCategories
FROM   products;
  
-- 2) Với mỗi category, sinh đơn hàng cho top 10 sản phẩm
DECLARE @cat    NVARCHAR(100);
DECLARE @pid    INT;
DECLARE @i      INT;
DECLARE @randUser INT;
DECLARE @orderId  INT;

DECLARE cat_cur CURSOR FOR
  SELECT category FROM #AllCategories;
OPEN cat_cur;
FETCH NEXT FROM cat_cur INTO @cat;

WHILE @@FETCH_STATUS = 0
BEGIN
  -- 2.1) Xác định top10 product_id theo id nhỏ nhất trong category
  SELECT TOP(10) id
  INTO   #Top10Prods
  FROM   products
  WHERE  category = @cat
  ORDER BY id ASC;

  -- 2.2) Với mỗi product trong #Top10Prods, tạo 3 đơn hàng
  DECLARE prod_cur CURSOR FOR
    SELECT product_id = id FROM #Top10Prods;
  OPEN prod_cur;
  FETCH NEXT FROM prod_cur INTO @pid;

  WHILE @@FETCH_STATUS = 0
  BEGIN
    SET @i = 1;
    WHILE @i <= 3
    BEGIN
      -- Chọn user_id random từ 2..7 (giả sử bạn có user mẫu)
      SET @randUser = ((ABS(CHECKSUM(NEWID())) % 6) + 2);

      -- 2.2.1) Tạo order với total_amount tạm tính 0 trước
      INSERT INTO orders (user_id, total_amount, payment_status, order_status)
      VALUES (@randUser, 0, 'Paid', 'Completed');
      SET @orderId = SCOPE_IDENTITY();

      -- 2.2.2) Tạo order_items: dùng variant_id = product_id
      INSERT INTO order_items (order_id, variant_id, quantity, unit_price)
      SELECT 
        @orderId,
        pv.id,  
        ((ABS(CHECKSUM(NEWID())) % 3) + 1),  -- quantity ngẫu nhiên 1..3
        p.price
      FROM product_variants pv
      JOIN products p ON p.id = pv.product_id
      WHERE p.id = @pid;

      -- 2.2.3) Cập nhật đúng total_amount của order
      UPDATE o
      SET    o.total_amount = oi.quantity * oi.unit_price
      FROM   orders o
      JOIN   order_items oi ON oi.order_id = o.id
      WHERE  o.id = @orderId;

      SET @i += 1;
    END

    FETCH NEXT FROM prod_cur INTO @pid;
  END

  CLOSE prod_cur;
  DEALLOCATE prod_cur;
  DROP TABLE #Top10Prods;

  FETCH NEXT FROM cat_cur INTO @cat;
END

CLOSE cat_cur;
DEALLOCATE cat_cur;
DROP TABLE #AllCategories;
GO

-- 3) Kiểm tra kết quả: tổng sold theo category
SELECT 
  p.category,
  p.id         AS product_id,
  p.name,
  SUM(oi.quantity) AS total_sold
FROM   orders o
JOIN   order_items oi      ON oi.order_id = o.id
JOIN   product_variants pv ON pv.id = oi.variant_id
JOIN   products p          ON p.id = pv.product_id
GROUP BY p.category, p.id, p.name
ORDER BY p.category, total_sold DESC;
GO
SELECT * FROM product_images
WHERE image_url LIKE '../%';
go
UPDATE product_images
SET image_url = REPLACE(image_url, '../', '');
delete from products;
DBCC CHECKIDENT ('products', RESEED, 0);
delete from product_images;

go
-- 1) Sinh 100 đơn hàng
DECLARE @i INT = 1;
WHILE @i <= 100
BEGIN
  -- 1.1) Ngày order ngẫu nhiên trong 365 ngày qua
  DECLARE @orderDate DATETIME = DATEADD(
    DAY,
    -CAST(RAND(CHECKSUM(NEWID())) * 365 AS INT),
    GETDATE()
  );

  -- 1.2) Chọn user_id ngẫu nhiên từ bảng users
  DECLARE @randomUser INT = (
    SELECT TOP 1 id
    FROM users
    ORDER BY NEWID()
  );

  -- 1.3) Tạo order tạm với total_amount = 0
  INSERT INTO orders (user_id, order_date, total_amount)
  VALUES (@randomUser, @orderDate, 0);
  DECLARE @orderId INT = SCOPE_IDENTITY();

  -- 1.4) Sinh 1–5 items cho order
  DECLARE @itemCount INT = CAST(RAND(CHECKSUM(NEWID())) * 5 + 1 AS INT);
  DECLARE @j INT = 1;
  WHILE @j <= @itemCount
  BEGIN
    -- a) Chọn ngẫu nhiên variant_id
    DECLARE @variantId INT = (
      SELECT TOP 1 id
      FROM product_variants
      ORDER BY NEWID()
    );
    -- b) Sinh quantity 1–10
    DECLARE @qty INT = CAST(RAND(CHECKSUM(NEWID())) * 10 + 1 AS INT);

    -- c) Lấy unit_price từ bảng products
    DECLARE @unitPrice DECIMAL(10,2);
    SELECT @unitPrice = p.price
    FROM product_variants pv
    JOIN products p
      ON pv.product_id = p.id
    WHERE pv.id = @variantId;

    -- d) Insert order_item
    INSERT INTO order_items (order_id, variant_id, quantity, unit_price)
    VALUES (@orderId, @variantId, @qty, @unitPrice);

    SET @j += 1;
  END

  -- 1.5) Cập nhật total_amount chính xác
  UPDATE orders
  SET total_amount = (
    SELECT SUM(quantity * unit_price)
    FROM order_items
    WHERE order_id = @orderId
  )
  WHERE id = @orderId;

  SET @i += 1;
END

go
-- Cập nhật quantity ngẫu nhiên từ 10–20 cho mỗi product_variant
UPDATE product_variants
SET quantity = (ABS(CHECKSUM(NEWID())) % 11) + 10;

