package io.github.Huduong123.user_service.mapper;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import io.github.Huduong123.user_service.dto.admin.account.AccountAdminDTO;
import io.github.Huduong123.user_service.dto.admin.account.AccountUpdateAdminDTO;
import io.github.Huduong123.user_service.dto.admin.authorities.AuthorityDTO;
import io.github.Huduong123.user_service.dto.auth.UserRegisterDTO;
import io.github.Huduong123.user_service.dto.auth.UserResponseDTO;
import io.github.Huduong123.user_service.dto.user.profile.UserProfileDTO;
import io.github.Huduong123.user_service.dto.user.profile.UserUpdateProfileDTO;
import io.github.Huduong123.user_service.entity.Authority;
import io.github.Huduong123.user_service.entity.User;

@Component
public class UserMapper {
    public UserResponseDTO converToDTO(User user) {
        Set<String> roles = user.getAuthorities().stream()
                .map(Authority::getAuthority)
                .collect(Collectors.toSet());
        return new UserResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFullname(),
                user.getPhone(),
                user.getGender(),
                user.getBirthDate(),
                user.isEnabled(),
                user.getCreatedAt(),
                roles);
    }

    public User convertToEntity(UserRegisterDTO dto, Set<Authority> authorities, String endcodedPassword) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(endcodedPassword);
        user.setEmail(dto.getEmail());
        user.setFullname(dto.getFullname());
        user.setPhone(dto.getPhone());
        // Handle nullable gender and birthDate fields
        if (dto.getGender() != null) {
            user.setGender(dto.getGender());
        }
        if (dto.getBirthDate() != null) {
            user.setBirthDate(dto.getBirthDate());
        }
        user.setEnabled(true);
        user.setCreatedAt(LocalDateTime.now());
        user.setAuthorities(new ArrayList<>(authorities));
        return user;
    }

    public UserProfileDTO convertToUserProfileDTO(User user) {
        return new UserProfileDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFullname(),
                user.getPhone(),
                user.getGender(),
                user.getBirthDate());
    }

    public void updateUserFromDTO(User user, UserUpdateProfileDTO userUpdateProfileDTO) {
        if (userUpdateProfileDTO.getEmail() != null) {
            user.setEmail(userUpdateProfileDTO.getEmail());
        }
        if (userUpdateProfileDTO.getFullname() != null) {
            user.setFullname(userUpdateProfileDTO.getFullname());
        }
        if (userUpdateProfileDTO.getPhone() != null) {
            user.setPhone(userUpdateProfileDTO.getPhone());
        }
        if (userUpdateProfileDTO.getGender() != null) {
            user.setGender(userUpdateProfileDTO.getGender());
        }
        if (userUpdateProfileDTO.getBirthday() != null) {
            user.setBirthDate(userUpdateProfileDTO.getBirthday());
        }

    }

    // Account
    public AccountAdminDTO convertToAdminDTO(User user) {
        // Convert authorities to AuthorityDTO list
        List<AuthorityDTO> authorityDTOs = user.getAuthorities().stream()
                .map(authority -> new AuthorityDTO(authority.getId(), authority.getAuthority()))
                .collect(Collectors.toList());

        return new AccountAdminDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFullname(),
                user.getPhone(),
                user.getGender(),
                user.getBirthDate(), // birthDate from entity maps to birthday in DTO
                user.isEnabled(),
                authorityDTOs,
                user.getCreatedAt(),
                user.getUpdatedAt());
    }

    public void updateAccountFromDTO(User user, AccountUpdateAdminDTO accountUpdateAdminDTO) {
        if (accountUpdateAdminDTO.getEmail() != null) {
            user.setEmail(accountUpdateAdminDTO.getEmail());
        }
        if (accountUpdateAdminDTO.getFullname() != null) {
            user.setFullname(accountUpdateAdminDTO.getFullname());
        }
        if (accountUpdateAdminDTO.getPhone() != null) {
            user.setPhone(accountUpdateAdminDTO.getPhone());
        }
        if (accountUpdateAdminDTO.getGender() != null) {
            user.setGender(accountUpdateAdminDTO.getGender());
        }
        if (accountUpdateAdminDTO.getBirthday() != null) {
            user.setBirthDate(accountUpdateAdminDTO.getBirthday());
        }
        user.setEnabled(accountUpdateAdminDTO.isEnabled());
    }
}
