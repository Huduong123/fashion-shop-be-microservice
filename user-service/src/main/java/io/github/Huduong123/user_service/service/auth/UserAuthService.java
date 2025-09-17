package io.github.Huduong123.user_service.service.auth;

import io.github.Huduong123.user_service.dto.auth.AdminLoginDTO;
import io.github.Huduong123.user_service.dto.auth.AdminLoginResponseDTO;
import io.github.Huduong123.user_service.dto.auth.UserLoginDTO;
import io.github.Huduong123.user_service.dto.auth.UserLoginResponseDTO;
import io.github.Huduong123.user_service.dto.auth.UserRegisterDTO;
import io.github.Huduong123.user_service.dto.auth.UserResponseDTO;

public interface UserAuthService {
    UserLoginResponseDTO login(UserLoginDTO userLoginDTO);

    AdminLoginResponseDTO loginAdmin(AdminLoginDTO adminLoginDTO);

    UserResponseDTO register(UserRegisterDTO userRegisterDTO);

}
