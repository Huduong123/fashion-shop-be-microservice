package io.github.Huduong123.user_service.service.common;

import java.util.Optional;

import io.github.Huduong123.user_service.entity.User;

public interface UserValidationService {
    boolean existsByPhone(String phone);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);
    Optional<User> findById(Long id);
    boolean isPasswordMatch(String rawPassword, String encodedPassword);

}
