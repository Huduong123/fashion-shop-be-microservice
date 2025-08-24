package io.github.Huduong123.user_service.dto.user.address;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAddressDTO {
    private Long id;
    private Long userId;
    private String recipientName;
    private String phoneNumber;
    private String addressDetail;
    private Boolean isDefault;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
