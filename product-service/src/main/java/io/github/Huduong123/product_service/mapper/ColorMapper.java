package io.github.Huduong123.product_service.mapper;

import org.springframework.stereotype.Component;

import io.github.Huduong123.product_service.dto.admin.color.ColorCreateDTO;
import io.github.Huduong123.product_service.dto.admin.color.ColorResponseDTO;
import io.github.Huduong123.product_service.dto.admin.color.ColorUpdateDTO;
import io.github.Huduong123.product_service.entity.Color;

@Component
public class ColorMapper {

    public ColorResponseDTO convertToDTO(Color color) {
        if (color == null) return null;

        return new ColorResponseDTO(
                color.getId(),
                color.getName(),
                color.getCreatedAt(),
                color.getUpdatedAt()
        );
    }

    public Color convertCreateDtoToEntity(ColorCreateDTO colorCreateDTO) {
        if (colorCreateDTO == null) return null;

        return Color.builder()
                .name(colorCreateDTO.getName())
                .build();
    }

    public void convertUpdateDtoToEntity(ColorUpdateDTO colorUpdateDTO, Color existingColer) {
        if (existingColer == null || colorUpdateDTO  == null) return ;


        existingColer.setName(colorUpdateDTO.getName());

    };
}
