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

import io.github.Huduong123.product_service.dto.admin.color.ColorCreateDTO;
import io.github.Huduong123.product_service.dto.admin.color.ColorResponseDTO;
import io.github.Huduong123.product_service.dto.admin.color.ColorUpdateDTO;
import io.github.Huduong123.product_service.dto.common.ResponseMessageDTO;
import io.github.Huduong123.product_service.service.admin.ColorService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/product/admin/colors")
public class ColorController {

    private final ColorService colorService;

    public ColorController(ColorService colorService) {
        this.colorService = colorService;
    }

    @GetMapping
    public ResponseEntity<List<ColorResponseDTO>> findAllColors() {
        List<ColorResponseDTO> colors = colorService.findAllColors();
        return ResponseEntity.ok(colors);
    }

    @GetMapping("/{colorId}")
    public ResponseEntity<ColorResponseDTO> findColorById(@PathVariable("colorId") long colorId) {
        ColorResponseDTO color = colorService.findColorById(colorId);
        return ResponseEntity.ok(color);
    }

    @PostMapping()
    public ResponseEntity<ColorResponseDTO> createColor(@Valid @RequestBody ColorCreateDTO colorCreateDTO) {
        ColorResponseDTO color = colorService.createColor(colorCreateDTO);
        return new ResponseEntity<>(color, HttpStatus.CREATED);
    }

    @PutMapping("/{colorId}")
    public ResponseEntity<ColorResponseDTO> updateColor(@PathVariable Long colorId,
                                                        @Valid @RequestBody ColorUpdateDTO colorUpdateDTO) {
        ColorResponseDTO colorResponseDTO = colorService.updateColorById(colorUpdateDTO,colorId);
        return  ResponseEntity.ok(colorResponseDTO);
    }

    @DeleteMapping("/{colorId}")
    public ResponseEntity<ResponseMessageDTO> deleteColor(@PathVariable long colorId) {
        ResponseMessageDTO responseMessageDTO = colorService.deleteColorById(colorId);
        return ResponseEntity.ok(responseMessageDTO);
    }
}
