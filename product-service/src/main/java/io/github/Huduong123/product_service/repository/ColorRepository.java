package io.github.Huduong123.product_service.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.github.Huduong123.product_service.entity.Color;

@Repository
public interface ColorRepository extends JpaRepository<Color, Long> {

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, Long id);
}
