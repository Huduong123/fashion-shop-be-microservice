package io.github.Huduong123.product_service.service.user;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.Huduong123.product_service.dto.admin.category.CategoryResponseDTO;
import io.github.Huduong123.product_service.mapper.CategoryMapper;
import io.github.Huduong123.product_service.repository.CategoryRepository;

@Service
public class UserCategoryServiceImp implements UserCategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public UserCategoryServiceImp(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponseDTO> findAll() {
        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::convertToDTO)
                .collect(Collectors.toList());
    }
}
