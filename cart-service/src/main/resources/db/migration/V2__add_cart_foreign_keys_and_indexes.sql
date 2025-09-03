-- Thêm khóa ngoại từ bảng cart_items đến bảng carts
-- Ràng buộc này đảm bảo tính toàn vẹn dữ liệu trong phạm vi của cart-service.
ALTER TABLE cart_items
ADD CONSTRAINT fk_cart_items_cart
FOREIGN KEY (cart_id) REFERENCES carts(id) ON DELETE CASCADE;

-- Tạo chỉ mục (index) để tối ưu hóa việc truy vấn các mặt hàng dựa trên giỏ hàng.
-- Chỉ mục trên carts.user_id đã được tự động tạo bởi ràng buộc UNIQUE.
CREATE INDEX idx_cart_items_on_cart_id ON cart_items(cart_id);