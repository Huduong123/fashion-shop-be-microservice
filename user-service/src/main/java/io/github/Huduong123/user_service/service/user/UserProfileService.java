package io.github.Huduong123.user_service.service.user;
import io.github.Huduong123.user_service.dto.common.ResponseMessageDTO;
import io.github.Huduong123.user_service.dto.user.profile.UserChangePasswordDTO;
import io.github.Huduong123.user_service.dto.user.profile.UserProfileDTO;
import io.github.Huduong123.user_service.dto.user.profile.UserUpdateProfileDTO;

public interface UserProfileService {
    UserProfileDTO getUserprofile(String username);
    UserProfileDTO updateUserProfile(String username, UserUpdateProfileDTO userUpdateProfileDTO);

    ResponseMessageDTO changePassword(String username, UserChangePasswordDTO userChangePasswordDTO);


}
