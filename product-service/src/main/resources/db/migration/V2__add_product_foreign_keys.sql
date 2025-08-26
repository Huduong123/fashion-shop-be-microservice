-- V2__add_product_foreign_keys.sql (Dành cho Product Service)

-- Khóa ngoại cho categories (tự tham chiếu để tạo cây danh mục)
ALTER TABLE categories ADD CONSTRAINT fk_categories_parent_id FOREIGN KEY (parent_id) REFERENCES categories(id) ON DELETE SET NULL;

-- Khóa ngoại từ products đến categories
ALTER TABLE products ADD CONSTRAINT fk_products_category FOREIGN KEY (category_id) REFERENCES categories (id);

-- Khóa ngoại cho product_variants
ALTER TABLE product_variants ADD CONSTRAINT fk_variant_product FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE;
ALTER TABLE product_variants ADD CONSTRAINT fk_variant_color FOREIGN KEY (color_id) REFERENCES colors(id);

-- Khóa ngoại cho product_variant_sizes
ALTER TABLE product_variant_sizes ADD CONSTRAINT fk_pvs_variant FOREIGN KEY (product_variant_id) REFERENCES product_variants(id) ON DELETE CASCADE;
ALTER TABLE product_variant_sizes ADD CONSTRAINT fk_pvs_size FOREIGN KEY (size_id) REFERENCES sizes(id) ON DELETE CASCADE;

-- Khóa ngoại cho product_variant_images
ALTER TABLE product_variant_images ADD CONSTRAINT fk_pvi_variant FOREIGN KEY (product_variant_id) REFERENCES product_variants(id) ON DELETE CASCADE;

-- Khóa ngoại cho reviews
ALTER TABLE reviews ADD CONSTRAINT fk_reviews_product FOREIGN KEY (product_id) REFERENCES products (id);
-- Chú ý: Không tạo khóa ngoại từ reviews.user_id đến users.id vì chúng ở 2 database khác nhau.