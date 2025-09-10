-- Bảng chính chứa thông tin tổng quan của đơn hàng
CREATE TABLE orders (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_code VARCHAR(20) NOT NULL UNIQUE COMMENT 'Mã đơn hàng duy nhất, thân thiện với người dùng',
    user_id BIGINT NOT NULL COMMENT 'ID của người dùng từ User Service, không có khóa ngoại',
    total_price DECIMAL(10,2) NOT NULL COMMENT 'Tổng giá trị của các sản phẩm trong đơn hàng',
    status ENUM('PENDING','CONFIRMED', 'SHIPPING', 'DELIVERED', 'COMPLETED', 'CANCELLED') NOT NULL DEFAULT 'PENDING' COMMENT 'Trạng thái xử lý của đơn hàng',
    payment_status ENUM('UNPAID', 'PAID', 'FAILED', 'REFUNDED') NOT NULL DEFAULT 'UNPAID' COMMENT 'Trạng thái thanh toán của đơn hàng',
    payment_method_code VARCHAR(50) NOT NULL COMMENT 'Mã của phương thức thanh toán, được sao chép lại',
    payment_method_name VARCHAR(100) NOT NULL COMMENT 'Tên của phương thức thanh toán, được sao chép lại',
    note TEXT COMMENT 'Ghi chú của khách hàng khi đặt hàng',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Bảng chứa các sản phẩm cụ thể trong một đơn hàng
CREATE TABLE order_items (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_id BIGINT NOT NULL COMMENT 'Tham chiếu đến đơn hàng chứa sản phẩm này',

    -- --- Dữ liệu tham chiếu từ Product Service ---
    product_id BIGINT NOT NULL COMMENT 'ID của sản phẩm gốc từ Product Service',
    product_variant_id BIGINT NOT NULL COMMENT 'ID của biến thể sản phẩm (theo màu) từ Product Service',
    size_id BIGINT NOT NULL COMMENT 'ID của kích cỡ từ Product Service',

    -- --- Dữ liệu được "snapshot" lại để đảm bảo tính lịch sử ---
    product_name VARCHAR(255) NOT NULL COMMENT 'Tên sản phẩm tại thời điểm mua',
    color_name VARCHAR(50) COMMENT 'Tên màu sắc tại thời điểm mua',
    size_name VARCHAR(20) NOT NULL COMMENT 'Tên kích cỡ tại thời điểm mua',
    image_url VARCHAR(255) COMMENT 'URL hình ảnh của biến thể tại thời điểm mua',
    quantity INT NOT NULL CHECK (quantity > 0),
    price DECIMAL(10,2) NOT NULL COMMENT 'Giá của sản phẩm tại thời điểm mua, cực kỳ quan trọng!',

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Bảng chứa thông tin giao hàng của đơn hàng (snapshot từ User Service)
CREATE TABLE order_shipping_details (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_id BIGINT NOT NULL UNIQUE COMMENT 'Mỗi đơn hàng chỉ có một thông tin giao hàng',
    recipient_name VARCHAR(100) NOT NULL COMMENT 'Tên người nhận, sao chép từ địa chỉ của người dùng',
    phone_number VARCHAR(20) NOT NULL COMMENT 'SĐT người nhận, sao chép từ địa chỉ của người dùng',
    address_detail TEXT NOT NULL COMMENT 'Địa chỉ chi tiết, sao chép từ địa chỉ của người dùng',
    shipping_fee DECIMAL(10,2) DEFAULT 0.00 COMMENT 'Phí vận chuyển cho đơn hàng',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Bảng master chứa các phương thức thanh toán có sẵn
CREATE TABLE payment_methods (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    code VARCHAR(50) NOT NULL UNIQUE COMMENT 'Mã định danh duy nhất, dạng slug, dùng trong code',
    name VARCHAR(100) NOT NULL COMMENT 'Tên hiển thị ngắn gọn cho người dùng',
    description VARCHAR(255) NOT NULL COMMENT 'Mô tả chi tiết về phương thức thanh toán',
    image_url VARCHAR(255) NULL COMMENT 'URL hình ảnh đại diện của phương thức thanh toán',
    is_enabled TINYINT(1) NOT NULL DEFAULT 1,
    type ENUM('OFFLINE', 'ONLINE_REDIRECT') NOT NULL COMMENT 'Loại phương thức thanh toán',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Bảng ghi lại các giao dịch thanh toán (cho các cổng thanh toán online)
CREATE TABLE payment_transactions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_id BIGINT NOT NULL COMMENT 'Liên kết tới đơn hàng',
    transaction_code VARCHAR(50) NOT NULL UNIQUE COMMENT 'Mã giao dịch duy nhất do hệ thống của mình tạo ra',
    gateway_transaction_id VARCHAR(100) COMMENT 'Mã giao dịch từ cổng thanh toán (MoMo, VNPay,...)',
    amount DECIMAL(10,2) NOT NULL COMMENT 'Số tiền của giao dịch',
    status ENUM('PENDING', 'SUCCESS', 'FAILED', 'CANCELLED') NOT NULL DEFAULT 'PENDING' COMMENT 'Trạng thái giao dịch',
    payment_method_code VARCHAR(50) NOT NULL COMMENT 'Mã của phương thức thanh toán được sử dụng',
    note TEXT COMMENT 'Ghi chú thêm hoặc thông báo lỗi từ cổng thanh toán',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);