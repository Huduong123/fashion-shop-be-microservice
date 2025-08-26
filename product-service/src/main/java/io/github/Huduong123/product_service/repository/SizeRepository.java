package io.github.Huduong123.product_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.github.Huduong123.product_service.entity.Size;

@Repository
public interface SizeRepository extends JpaRepository<Size, Long> {

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, Long id);
}
