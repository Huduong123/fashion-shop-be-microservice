package io.github.Huduong123.product_service.service.user;

import java.util.List;

import io.github.Huduong123.product_service.dto.admin.color.ColorResponseDTO;

public interface UserColorService {
    List<ColorResponseDTO> getAllColors();
}
