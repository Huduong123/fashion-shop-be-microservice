package io.github.Huduong123.product_service.controller.user;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.Huduong123.product_service.dto.admin.color.ColorResponseDTO;
import io.github.Huduong123.product_service.service.user.UserColorService;


@RestController
@RequestMapping("/api/v1/product/colors")

public class UserColorController {
    private final UserColorService userColorService;

    public UserColorController(UserColorService userColorService) {
        this.userColorService = userColorService;
    }
    
    @GetMapping
    public ResponseEntity<List<ColorResponseDTO>> getAllColors() {
        List<ColorResponseDTO> colors = userColorService.getAllColors();
        return ResponseEntity.ok(colors);
    }
}