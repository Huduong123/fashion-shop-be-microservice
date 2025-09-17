package io.github.Huduong123.user_service.controller.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.Huduong123.user_service.dto.auth.UserLoginDTO;
import io.github.Huduong123.user_service.dto.auth.UserLoginResponseDTO;
import io.github.Huduong123.user_service.dto.auth.UserRegisterDTO;
import io.github.Huduong123.user_service.dto.auth.UserResponseDTO;
import io.github.Huduong123.user_service.service.auth.UserAuthService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/user/auth")
public class UserAuthController {

    private final UserAuthService userAuthService;

    @Autowired
    public UserAuthController(UserAuthService userAuthService) {
        this.userAuthService = userAuthService;
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponseDTO> login(@RequestBody @Valid UserLoginDTO userLoginDTO) {
        try {
            UserLoginResponseDTO response = userAuthService.login(userLoginDTO);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            throw e; // Let GlobalExceptionHandler handle this
        }
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@RequestBody @Valid UserRegisterDTO userRegisterDTO) {
        try {
            UserResponseDTO userResponse = userAuthService.register(userRegisterDTO);
            return ResponseEntity.ok(userResponse);
        } catch (Exception e) {
            throw e; // Let GlobalExceptionHandler handle this
        }
    }
}
