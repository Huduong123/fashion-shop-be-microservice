package io.github.Huduong123.product_service.service.admin;

import java.util.List;

import io.github.Huduong123.product_service.dto.admin.color.ColorCreateDTO;
import io.github.Huduong123.product_service.dto.admin.color.ColorResponseDTO;
import io.github.Huduong123.product_service.dto.admin.color.ColorUpdateDTO;
import io.github.Huduong123.product_service.dto.common.ResponseMessageDTO;


public interface ColorService {

    List<ColorResponseDTO> findAllColors();

    ColorResponseDTO findColorById(Long colorId);

    ColorResponseDTO createColor(ColorCreateDTO colorCreateDTO);

    ColorResponseDTO updateColorById(ColorUpdateDTO colorUpdateDTO, Long colorId);
    ResponseMessageDTO deleteColorById(Long colorId);
}
