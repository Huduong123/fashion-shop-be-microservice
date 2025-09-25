package io.github.Huduong123.user_service.controller.user;

import java.security.Principal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.Huduong123.user_service.dto.common.ResponseMessageDTO;
import io.github.Huduong123.user_service.dto.user.address.UserAddressDTO;
import io.github.Huduong123.user_service.dto.user.address.UserCreateAddressDTO;
import io.github.Huduong123.user_service.dto.user.address.UserUpdateAddressDTO;
import io.github.Huduong123.user_service.service.user.UserAddressService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/user/address")
public class UserAddressController {

    private final UserAddressService userAddressService;

    public UserAddressController(UserAddressService userAddressService) {
        this.userAddressService = userAddressService;
    }

    /**
     * Lấy tất cả địa chỉ của người dùng đang đăng nhập.
     */
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<UserAddressDTO>> getCurrentUserAddresses(Principal principal) {
        String username = principal.getName();
        List<UserAddressDTO> addresses = userAddressService.findAllAddressesForUser(username);
        return ResponseEntity.ok(addresses);
    }

    /**
     * Lấy một địa chỉ cụ thể của người dùng đang đăng nhập theo ID.
     * Hệ thống sẽ tự kiểm tra xem địa chỉ này có thuộc về họ không.
     */
    @GetMapping("/{addressId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserAddressDTO> getAddressById(@PathVariable Long addressId, Principal principal) {
        String username = principal.getName();
        UserAddressDTO address = userAddressService.findAddressById(addressId, username);
        return ResponseEntity.ok(address);
    }

    /**
     * Tạo một địa chỉ mới cho người dùng đang đăng nhập.
     */
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserAddressDTO> createAddress(@Valid @RequestBody UserCreateAddressDTO createAddressDTO,
            Principal principal) {
        String username = principal.getName();
        UserAddressDTO createdAddress = userAddressService.createAddress(createAddressDTO, username);
        return new ResponseEntity<>(createdAddress, HttpStatus.CREATED);
    }

    /**
     * Cập nhật địa chỉ cho người dùng đang đăng nhập.
     * Hệ thống sẽ tự kiểm tra xem địa chỉ này có thuộc về họ không.
     */
    @PutMapping("/{addressId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserAddressDTO> updateAddress(@PathVariable Long addressId,
            @Valid @RequestBody UserUpdateAddressDTO updateAddressDTO,
            Principal principal) {
        String username = principal.getName();
        UserAddressDTO updatedAddress = userAddressService.updateAddress(addressId, updateAddressDTO, username);
        return ResponseEntity.ok(updatedAddress);
    }

    /**
     * Xóa một địa chỉ của người dùng đang đăng nhập.
     */
    @DeleteMapping("/{addressId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ResponseMessageDTO> deleteAddress(@PathVariable Long addressId, Principal principal) {
        String username = principal.getName();
        ResponseMessageDTO response = userAddressService.deleteAddress(addressId, username);
        return ResponseEntity.ok(response);
    }
}
