package io.github.Huduong123.product_service.mapper;

import org.springframework.stereotype.Component;

import io.github.Huduong123.product_service.dto.admin.size.SizeCreateDTO;
import io.github.Huduong123.product_service.dto.admin.size.SizeResponseDTO;
import io.github.Huduong123.product_service.dto.admin.size.SizeUpdateDTO;
import io.github.Huduong123.product_service.entity.Size;

@Component
public class SizeMapper {

    public SizeResponseDTO convertToDTO(Size size) {
        if (size == null) {
            return null;
        }
        return  new SizeResponseDTO(
                size.getId(),
                size.getName(),
                size.getCreatedAt(),
                size.getUpdatedAt()
        );
    }


    public Size convertCreateDTOToEntity(SizeCreateDTO sizeCreateDTO) {
        if (sizeCreateDTO == null) {
            return null;
        }


        return  Size.builder()
                .name(sizeCreateDTO.getName())
                .build();
    }

    public void convertUpdateDTOToEntity(Size existingSize, SizeUpdateDTO sizeUpdateDTO) {
        if (existingSize == null || sizeUpdateDTO == null) {
            return;
        }

        existingSize.setName(sizeUpdateDTO.getName());
    }
}
