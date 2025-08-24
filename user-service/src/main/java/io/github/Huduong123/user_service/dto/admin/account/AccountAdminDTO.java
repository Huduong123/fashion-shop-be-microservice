package io.github.Huduong123.user_service.dto.admin.account;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import io.github.Huduong123.user_service.dto.admin.authorities.AuthorityDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountAdminDTO {
    private Long id;
    private String username;
    private String email;
    private String fullname;
    private String phone;
    private String gender;
    private LocalDate birthday;
    private boolean enabled;
    private List<AuthorityDTO> authorities;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
