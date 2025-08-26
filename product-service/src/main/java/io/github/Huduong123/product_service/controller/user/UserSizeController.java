package io.github.Huduong123.product_service.controller.user;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.Huduong123.product_service.dto.admin.size.SizeResponseDTO;
import io.github.Huduong123.product_service.service.user.UserSizeService;


@RestController
@RequestMapping("/api/v1/product/sizes") 
public class UserSizeController {
    private final UserSizeService userSizeService;

    public UserSizeController(UserSizeService userSizeService) {
        this.userSizeService = userSizeService;
    }

    @GetMapping
    public ResponseEntity<List<SizeResponseDTO>> getAllSizes() {
        List<SizeResponseDTO> sizes = userSizeService.getAllSizes();
        return ResponseEntity.ok(sizes);
    }
}
