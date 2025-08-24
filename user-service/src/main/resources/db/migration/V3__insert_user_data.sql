
-- 1. Chèn dữ liệu người dùng và quyền hạn
INSERT INTO users (id, username, password, email, full_name, phone, gender, birth_date) VALUES
(1, 'admin', '$2a$12$H7YNqN.rFijke07DqC.eTuu0n70mZN22wjGWS5NAuVOEpZUMor0Va', 'admin@example.com', 'Admin User', '0901234567', 'MALE', '1980-01-01'),
(2, 'john_doe', '$2a$12$H7YNqN.rFijke07DqC.eTuu0n70mZN22wjGWS5NAuVOEpZUMor0Va', 'john@example.com', 'John Doe', '0902345678', 'MALE', '1990-03-10'),
(3, 'system', '$2a$12$H7YNqN.rFijke07DqC.eTuu0n70mZN22wjGWS5NAuVOEpZUMor0Va', 'system@example.com', 'System', '0903456789', 'MALE', '1995-07-15');

INSERT INTO authorities (user_id, authority) VALUES
(1, 'ROLE_ADMIN'),
(2, 'ROLE_USER'),
(3, 'ROLE_SYSTEM');