package io.github.Huduong123.user_service.service.admin;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import io.github.Huduong123.user_service.dto.admin.account.AccountAdminDTO;
import io.github.Huduong123.user_service.dto.admin.account.AccountCreateAdminDTO;
import io.github.Huduong123.user_service.dto.admin.account.AccountUpdateAdminDTO;

public interface AccountService {
    List<AccountAdminDTO> getAllAccount();

    AccountAdminDTO createAccount(AccountCreateAdminDTO accountCreateAdminDTO);

    AccountAdminDTO updateAccount(AccountUpdateAdminDTO accountUpdateAdminDTO);

    void deleteAccount(Long userId);

    AccountAdminDTO getAccountById(Long id);

    List<AccountAdminDTO> searchAccounts(
            String username,
            String email,
            String fullname,
            String phone,
            String gender,
            LocalDate birthday,
            Boolean enabled,
            String role,
            LocalDateTime createdAtStart,
            LocalDateTime createdAtEnd,
            LocalDateTime updatedAtStart,
            LocalDateTime updatedAtEnd);

    AccountAdminDTO toggleAccountStatus(Long accountId, Boolean enabled);

    AccountAdminDTO addRoleToUser(Long userId, String roleName);

    AccountAdminDTO removeRoleFromUser(Long userId, String roleName);
}
