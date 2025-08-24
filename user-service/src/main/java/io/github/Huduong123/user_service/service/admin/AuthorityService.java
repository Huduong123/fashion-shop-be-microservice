package io.github.Huduong123.user_service.service.admin;

import java.util.List;

import io.github.Huduong123.user_service.dto.admin.authorities.AuthorityCreateDTO;
import io.github.Huduong123.user_service.dto.admin.authorities.AuthorityResponseDTO;
import io.github.Huduong123.user_service.dto.admin.authorities.AuthorityUpdateDTO;

public interface AuthorityService {

    List<AuthorityResponseDTO> findAll();

    AuthorityResponseDTO createAuthority(AuthorityCreateDTO authorityCreateDTO);

    AuthorityResponseDTO updateAuthority(AuthorityUpdateDTO authorityUpdateDTO);

    void deleteAuthority(Long authorityId);

    List<AuthorityResponseDTO> search(String username, String authority);


}
