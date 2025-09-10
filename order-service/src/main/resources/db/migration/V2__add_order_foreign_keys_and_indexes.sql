-- Khóa ngoại từ order_items -> orders
ALTER TABLE order_items
ADD CONSTRAINT fk_order_items_order
FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE;

-- Khóa ngoại từ order_shipping_details -> orders
ALTER TABLE order_shipping_details
ADD CONSTRAINT fk_shipping_details_order
FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE;

-- Khóa ngoại từ payment_transactions -> orders
ALTER TABLE payment_transactions
ADD CONSTRAINT fk_transactions_order
FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE;

-- --- Tạo các chỉ mục (Indexes) để tăng tốc độ truy vấn ---

-- Chỉ mục trên user_id trong bảng orders để tìm tất cả đơn hàng của một người dùng
CREATE INDEX idx_orders_user_id ON orders(user_id);

-- Chỉ mục trên order_id trong bảng order_items
CREATE INDEX idx_order_items_order_id ON order_items(order_id);

-- Chỉ mục trên order_id trong bảng payment_transactions
CREATE INDEX idx_transactions_order_id ON payment_transactions(order_id);

-- Chỉ mục trên mã giao dịch của cổng thanh toán để xử lý callback
CREATE INDEX idx_transactions_gateway_id ON payment_transactions(gateway_transaction_id);