-- 创建产品表
CREATE TABLE products (
                          product_id SERIAL PRIMARY KEY,
                          product_name VARCHAR(100),
                          price DECIMAL(10, 2),
                          category_id INT,
                          manufacturer_id INT
);

-- 创建产品类别表
CREATE TABLE categories (
                            category_id SERIAL PRIMARY KEY,
                            category_name VARCHAR(50)
);

-- 创建制造商表
CREATE TABLE manufacturers (
                               manufacturer_id SERIAL PRIMARY KEY,
                               manufacturer_name VARCHAR(100),
                               country VARCHAR(50)
);

-- 创建订单表
CREATE TABLE orders (
                        order_id SERIAL PRIMARY KEY,
                        customer_id INT,
                        order_date DATE,
                        total_amount DECIMAL(10, 2)
);

-- 创建订单详情表
CREATE TABLE order_details (
                               detail_id SERIAL PRIMARY KEY,
                               order_id INT,
                               product_id INT,
                               quantity INT,
                               subtotal DECIMAL(10, 2)
);

CREATE TABLE Department (
                            id SERIAL PRIMARY KEY,
                            name VARCHAR(100) NOT NULL
);