package io.github.Huduong123.product_service.service.admin;

import java.util.List;

import io.github.Huduong123.product_service.dto.admin.size.SizeCreateDTO;
import io.github.Huduong123.product_service.dto.admin.size.SizeResponseDTO;
import io.github.Huduong123.product_service.dto.admin.size.SizeUpdateDTO;
import io.github.Huduong123.product_service.dto.common.ResponseMessageDTO;

public interface SizeService {

    List<SizeResponseDTO> getAllSize();

    SizeResponseDTO getSizeById(Long id);

    SizeResponseDTO create(SizeCreateDTO createDTO);

    SizeResponseDTO update(SizeUpdateDTO updateDTO, Long id);

    ResponseMessageDTO delete(Long id);
}
