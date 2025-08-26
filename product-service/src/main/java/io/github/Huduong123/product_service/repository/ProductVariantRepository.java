package io.github.Huduong123.product_service.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import io.github.Huduong123.product_service.entity.ProductVariant;

@Repository
public interface ProductVariantRepository extends JpaRepository<ProductVariant, Long> {

    // Kiểm tra xem màu có đang được sử dụng bởi variant nào không
    @Query("SELECT COUNT(pv) > 0 FROM ProductVariant pv WHERE pv.color.id = :colorId")
    boolean existsByColorId(@Param("colorId") Long colorId);

    // Kiểm tra xem size có đang được sử dụng bởi variant nào không
    @Query("SELECT COUNT(pvs) > 0 FROM ProductVariantSize pvs WHERE pvs.size.id = :sizeId")
    boolean existsBySizeId(@Param("sizeId") Long sizeId);

    // Đếm số sản phẩm sử dụng màu
    @Query("SELECT COUNT(DISTINCT pv.product) FROM ProductVariant pv WHERE pv.color.id = :colorId")
    long countProductsByColorId(@Param("colorId") Long colorId);

    // Đếm số sản phẩm sử dụng size
    @Query("SELECT COUNT(DISTINCT pvs.productVariant.product) FROM ProductVariantSize pvs WHERE pvs.size.id = :sizeId")
    long countProductsBySizeId(@Param("sizeId") Long sizeId);

    /**
     * TẢI DỮ LIỆU BỔ SUNG: Lấy danh sách các ProductVariant cùng với
     * ProductVariantSizes của chúng.
     */
    @Query("SELECT DISTINCT pv FROM ProductVariant pv " +
           "LEFT JOIN FETCH pv.productVariantSizes pvs " +
           "WHERE pv IN :variants")
    List<ProductVariant> findWithSizesByVariants(@Param("variants") List<ProductVariant> variants);
}
