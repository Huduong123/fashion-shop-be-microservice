package io.github.Huduong123.cart_service.entity;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cart_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItem extends BaseEntity{
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable= false)
    @JsonBackReference
    private Cart cart;

    @Column(name = "product_id", nullable= false)
    private Long productId;

    @Column(name = "product_variant_id", nullable= false)
    private Long productVariantId;

    @Column(name = "size_id", nullable= false)
    private Long sizeId;

       // --- Dữ liệu được phi chuẩn hóa (denormalized) để tối ưu hiển thị ---
       @Column(name = "product_name", nullable = false)
       private String productName;
   
       @Column(name = "color_name")
       private String colorName;
   
       @Column(name = "size_name", nullable = false)
       private String sizeName;
   
       @Column(name = "image_url")
       private String imageUrl;

       @Column(name = "price", nullable = false, precision = 10, scale = 2)
       private BigDecimal price;

       @Column(name = "quantity", nullable = false)
       private Integer quantity;   
}
