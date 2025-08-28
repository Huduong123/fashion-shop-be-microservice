package io.github.Huduong123.user_service.controller.user;

import java.security.Principal;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.Huduong123.user_service.dto.common.ResponseMessageDTO;
import io.github.Huduong123.user_service.dto.user.profile.UserChangePasswordDTO;
import io.github.Huduong123.user_service.dto.user.profile.UserProfileDTO;
import io.github.Huduong123.user_service.dto.user.profile.UserUpdateProfileDTO;
import io.github.Huduong123.user_service.service.user.UserProfileService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/user/profile")
public class
UserProfileController {
    private final UserProfileService userProfileService;

    public UserProfileController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }



    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserProfileDTO> getCurentUserProfile(Principal principal) {
        String username = principal.getName();
        UserProfileDTO userProfile = userProfileService.getUserprofile(username);
        return ResponseEntity.ok(userProfile);
    }

    @PutMapping
    public ResponseEntity<UserProfileDTO> updateCurrentUserProfile(
            @RequestBody @Valid UserUpdateProfileDTO updateProfileDTO
            , Principal principal) {
        String username = principal.getName();
        UserProfileDTO updatedUserProfile= userProfileService.updateUserProfile(username, updateProfileDTO);
        return ResponseEntity.ok(updatedUserProfile);
    }

    @PutMapping("/change-password")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ResponseMessageDTO> changePassword(
            @RequestBody @Valid UserChangePasswordDTO  userChangePasswordDTO,
            Principal principal
            ) {
        String username = principal.getName();
        ResponseMessageDTO responseMessageDTO = userProfileService.changePassword(username, userChangePasswordDTO);
        return ResponseEntity.ok(responseMessageDTO);
    }
}
