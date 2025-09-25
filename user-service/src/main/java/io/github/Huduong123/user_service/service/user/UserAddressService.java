package io.github.Huduong123.user_service.service.user;

import java.util.List;

import io.github.Huduong123.user_service.dto.common.ResponseMessageDTO;
import io.github.Huduong123.user_service.dto.user.address.UserAddressDTO;
import io.github.Huduong123.user_service.dto.user.address.UserCreateAddressDTO;
import io.github.Huduong123.user_service.dto.user.address.UserUpdateAddressDTO;

public interface UserAddressService {
    List<UserAddressDTO> findAllAddressesForUser(String username);

    UserAddressDTO findAddressById(Long addressId, String username);

    UserAddressDTO createAddress(UserCreateAddressDTO createAddressDTO, String username);

    UserAddressDTO updateAddress(Long addressId, UserUpdateAddressDTO updateAddressDTO, String username);

    ResponseMessageDTO deleteAddress(Long addressId, String username);

}
