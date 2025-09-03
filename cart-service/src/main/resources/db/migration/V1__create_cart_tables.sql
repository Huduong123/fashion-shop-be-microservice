-- Bảng giỏ hàng (carts): Mỗi người dùng sẽ có một giỏ hàng duy nhất.
CREATE TABLE carts (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'Khóa chính của giỏ hàng',
    user_id BIGINT NOT NULL UNIQUE COMMENT 'ID của người dùng từ User Service, không có khóa ngoại',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Bảng các mặt hàng trong giỏ hàng (cart_items)
CREATE TABLE cart_items (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'Khóa chính của mục trong giỏ hàng',
    cart_id BIGINT NOT NULL COMMENT 'Khóa ngoại tham chiếu đến bảng carts',

    -- --- Dữ liệu tham chiếu từ Product Service ---
    product_id BIGINT NOT NULL COMMENT 'ID của sản phẩm gốc từ Product Service',
    product_variant_id BIGINT NOT NULL COMMENT 'ID của biến thể sản phẩm (theo màu) từ Product Service',
    size_id BIGINT NOT NULL COMMENT 'ID của kích cỡ từ Product Service',

    -- --- Dữ liệu được phi chuẩn hóa (denormalized) để tối ưu hiển thị ---
    product_name VARCHAR(255) NOT NULL COMMENT 'Tên sản phẩm',
    color_name VARCHAR(50) COMMENT 'Tên màu sắc',
    size_name VARCHAR(20) NOT NULL COMMENT 'Tên kích cỡ',
    image_url VARCHAR(255) COMMENT 'URL hình ảnh của biến thể',
    price DECIMAL(10, 2) NOT NULL COMMENT 'Giá của sản phẩm tại thời điểm được thêm vào giỏ hàng',

    -- --- Dữ liệu chính của cart_item ---
    quantity INT NOT NULL CHECK (quantity > 0),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    -- Ràng buộc để đảm bảo mỗi loại sản phẩm (variant + size) chỉ xuất hiện một lần trong một giỏ hàng.
    -- Nếu người dùng thêm lại, ta nên cập nhật số lượng (quantity) thay vì thêm dòng mới.
    CONSTRAINT uq_cart_item UNIQUE (cart_id, product_variant_id, size_id)
);