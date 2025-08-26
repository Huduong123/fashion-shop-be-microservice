package io.github.Huduong123.product_service.controller.admin;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.Huduong123.product_service.dto.admin.size.SizeCreateDTO;
import io.github.Huduong123.product_service.dto.admin.size.SizeResponseDTO;
import io.github.Huduong123.product_service.dto.admin.size.SizeUpdateDTO;
import io.github.Huduong123.product_service.dto.common.ResponseMessageDTO;
import io.github.Huduong123.product_service.service.admin.SizeService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin/sizes")
public class SizeController {

    private final SizeService sizeService;

    public SizeController(SizeService sizeService) {
        this.sizeService = sizeService;
    }

    @GetMapping
    public ResponseEntity<List<SizeResponseDTO>> getAllSizes() {
        List<SizeResponseDTO> sizes = sizeService.getAllSize();
        return  ResponseEntity.ok(sizes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SizeResponseDTO> getSizeById(@PathVariable("id") long id) {
        SizeResponseDTO size = sizeService.getSizeById(id);
        return  ResponseEntity.ok(size);
    }

    @PostMapping()
    public ResponseEntity<SizeResponseDTO> createSize(@Valid @RequestBody SizeCreateDTO sizeCreateDTO) {
        SizeResponseDTO size = sizeService.create(sizeCreateDTO);
        return  new ResponseEntity<>(size, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SizeResponseDTO> updateSize(@PathVariable("id") long id, @Valid @RequestBody SizeUpdateDTO sizeUpdateDTO) {
        SizeResponseDTO sizeResponseDTO = sizeService.update(sizeUpdateDTO, id);
        return  ResponseEntity.ok(sizeResponseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseMessageDTO> deleteSize(@PathVariable("id") long id) {
        ResponseMessageDTO responseMessageDTO = sizeService.delete(id);
        return  ResponseEntity.ok(responseMessageDTO);
    }
}
