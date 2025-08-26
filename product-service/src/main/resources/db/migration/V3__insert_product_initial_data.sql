-- V3__insert_product_initial_data.sql (Dành cho Product Service)

-- 1. Chèn dữ liệu cho Categories
INSERT INTO categories (id, name, description, slug) VALUES
(1, 'Áo', 'Tất cả các loại áo thời trang', 'ao'),
(2, 'Quần', 'Quần jeans, quần short, quần dài...', 'quan'),
(3, 'Phụ kiện', 'Nón, dây nịt, túi xách...', 'phu-kien');

INSERT INTO categories (id, name, description, parent_id, slug) VALUES
(4, 'Áo Nam', 'Các loại áo dành cho nam giới', 1, 'ao-nam'),
(5, 'Quần Nam', 'Các loại quần dành cho nam giới', 2, 'quan-nam');

INSERT INTO categories (name, description, parent_id, type, status, slug) VALUES
('Áo polo nam', 'Áo polo dành cho nam giới', 4, 'LINK', 'ACTIVE', 'ao-polo-nam'),
('Áo thun nam', 'Áo thun tay ngắn, tay dài dành cho nam', 4, 'LINK', 'ACTIVE', 'ao-thun-nam'),
('Áo sơ mi nam', 'Áo sơ mi công sở và đi chơi dành cho nam', 4, 'LINK', 'ACTIVE', 'ao-so-mi-nam'),
('Áo khoác nam', 'Áo khoác, jacket dành cho nam', 4, 'LINK', 'ACTIVE', 'ao-khoac-nam'),
('Quần jean nam', 'Quần jean dành cho nam giới', 5, 'LINK', 'ACTIVE', 'quan-jean-nam'),
('Quần tây nam', 'Quần tây công sở dành cho nam', 5, 'LINK', 'ACTIVE', 'quan-tay-nam'),
('Quần short nam', 'Quần short mùa hè dành cho nam', 5, 'LINK', 'ACTIVE', 'quan-short-nam');

-- 2. Chèn dữ liệu Colors và Sizes
INSERT INTO colors (id, name) VALUES (1, 'Đen'), (2, 'Trắng'), (3, 'Xanh Navy'), (4, 'Xám');
INSERT INTO sizes (id, name) VALUES (1, 'S'), (2, 'M'), (3, 'L'), (4, 'XL');