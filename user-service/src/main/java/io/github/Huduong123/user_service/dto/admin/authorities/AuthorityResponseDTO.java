package io.github.Huduong123.user_service.dto.admin.authorities;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthorityResponseDTO {
    private Long id;
    private String username;
    private String authority;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
