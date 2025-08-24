package io.github.Huduong123.user_service.dto.user.profile;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDTO {
    private Long id;
    private String username;
    private String email;
    private String fullname;
    private String phone;
    private String gender;
    private LocalDate birthday;
}
