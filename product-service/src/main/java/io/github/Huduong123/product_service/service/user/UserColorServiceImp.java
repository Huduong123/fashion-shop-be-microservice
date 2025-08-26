package io.github.Huduong123.product_service.service.user;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import io.github.Huduong123.product_service.dto.admin.color.ColorResponseDTO;
import io.github.Huduong123.product_service.mapper.ColorMapper;
import io.github.Huduong123.product_service.repository.ColorRepository;

@Service
public class UserColorServiceImp implements UserColorService{

    private final ColorRepository colorRepository;
    private final ColorMapper colorMapper;

    public UserColorServiceImp(ColorRepository colorRepository, ColorMapper colorMapper) {
        this.colorRepository = colorRepository;
        this.colorMapper = colorMapper;
    }

    @Override
    public List<ColorResponseDTO> getAllColors() {
          // Sắp xếp theo tên để hiển thị nhất quán
    return colorRepository.findAll().stream()
            .sorted((c1, c2) -> c1.getName().compareToIgnoreCase(c2.getName()))
            .map(colorMapper::convertToDTO)
            .collect(Collectors.toList());
    }
    
}
