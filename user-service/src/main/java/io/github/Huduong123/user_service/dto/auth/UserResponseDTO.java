package io.github.Huduong123.user_service.dto.auth;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {
    Long id;
    private String username;
    private String email;
    private String fullname;
    private String phone;
    private String gender;
    private LocalDate birthDate;
    private Boolean enabled;
    private LocalDateTime createdAt;
    private Set<String> roles;
}
