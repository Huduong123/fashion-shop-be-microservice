-- Thêm dữ liệu ban đầu cho bảng payment_methods
INSERT INTO payment_methods(code, name, description, image_url, is_enabled, type) VALUES
('cod', 'Thanh toán khi nhận hàng (COD)', 'Thanh toán trực tiếp cho nhân viên giao hàng khi nhận sản phẩm.', '/icons/cod.png', 1, 'OFFLINE'),
('vnpay', 'VNPAY', 'Thanh toán qua cổng VNPAY (ATM/Visa/Master/JCB/QR Pay).', '/icons/vnpay.png', 1, 'ONLINE_REDIRECT'),
('momo', 'Ví MoMo', 'Thanh toán an toàn và nhanh chóng qua ví điện tử MoMo.', '/icons/momo.png', 1, 'ONLINE_REDIRECT');