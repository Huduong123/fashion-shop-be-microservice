package io.github.Huduong123.product_service.service.user;

import java.util.List;

import io.github.Huduong123.product_service.dto.admin.size.SizeResponseDTO;

public interface UserSizeService {
    
    List<SizeResponseDTO> getAllSizes();
}
