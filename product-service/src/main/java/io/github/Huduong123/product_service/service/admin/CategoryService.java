package io.github.Huduong123.product_service.service.admin;

import java.time.LocalDate;
import java.util.List;

import io.github.Huduong123.product_service.dto.admin.category.CategoryCreateDTO;
import io.github.Huduong123.product_service.dto.admin.category.CategoryResponseDTO;
import io.github.Huduong123.product_service.dto.admin.category.CategoryUpdateDTO;
import io.github.Huduong123.product_service.dto.common.ResponseMessageDTO;
import io.github.Huduong123.product_service.entity.enums.CategoryType;

public interface CategoryService {

    List<CategoryResponseDTO> findAll(String name, LocalDate createdAt, LocalDate updatedAt);

    List<CategoryResponseDTO> findAll(String name, LocalDate createdAt, LocalDate updatedAt, Long parentId,
            Boolean isRoot);

    List<CategoryResponseDTO> findAll(String name, LocalDate createdAt, LocalDate updatedAt, Long parentId,
            Boolean isRoot, CategoryType type);

    List<CategoryResponseDTO> findRootCategories();

    List<CategoryResponseDTO> findChildrenByParentId(Long parentId);

    CategoryResponseDTO findById(Long categoryId);

    CategoryResponseDTO findBySlug(String slug);

    CategoryResponseDTO createCategory(CategoryCreateDTO categoryCreateDTO);

    CategoryResponseDTO updateCategory(Long categoryId, CategoryUpdateDTO categoryUpdateDTO);

    ResponseMessageDTO deleteCategory(Long categoryId);

    // Additional hierarchy methods
    List<CategoryResponseDTO> getCategoryHierarchy();

    boolean canDeleteCategory(Long categoryId);

    List<CategoryResponseDTO> getCategoryPath(Long categoryId);
}
