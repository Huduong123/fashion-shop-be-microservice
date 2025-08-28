package io.github.Huduong123.user_service.service.auth;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import io.github.Huduong123.user_service.dto.auth.AdminLoginDTO;
import io.github.Huduong123.user_service.dto.auth.AdminLoginResponseDTO;
import io.github.Huduong123.user_service.dto.auth.UserLoginDTO;
import io.github.Huduong123.user_service.dto.auth.UserLoginResponseDTO;
import io.github.Huduong123.user_service.dto.auth.UserRegisterDTO;
import io.github.Huduong123.user_service.dto.auth.UserResponseDTO;
import io.github.Huduong123.user_service.entity.Authority;
import io.github.Huduong123.user_service.entity.User;
import io.github.Huduong123.user_service.exception.AuthenticationFailedException;
import io.github.Huduong123.user_service.mapper.UserMapper;
import io.github.Huduong123.user_service.repository.AuthorityRepository;
import io.github.Huduong123.user_service.repository.UserRepository;
import io.github.Huduong123.user_service.security.JwtUtil;
import io.github.Huduong123.user_service.service.common.UserValidationService;
import jakarta.transaction.Transactional;

@Service
public class UserAuthServiceImp implements UserAuthService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final AuthorityRepository authorityRepository;
    private final UserMapper userMapper;
    private final UserValidationService userValidationService;
    private static final Logger logger = LoggerFactory.getLogger(UserAuthServiceImp.class);

    public UserAuthServiceImp(UserRepository userRepository, JwtUtil jwtUtil, PasswordEncoder passwordEncoder,
            AuthorityRepository authorityRepository, UserMapper userMapper,
            UserValidationService userValidationService) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.authorityRepository = authorityRepository;
        this.userMapper = userMapper;
        this.userValidationService = userValidationService;
    }

    @Override
    @Transactional
    public UserLoginResponseDTO login(UserLoginDTO userLoginDTO) {
        // Tìm kiếm người dùng nhưng không ném lỗi ngay lập tức
        Optional<User> userOptional = userValidationService.findByUsername(userLoginDTO.getUsername());

        // Nếu không tìm thấy người dùng hoặc mật khẩu không khớp, ném ra cùng một lỗi
        if (userOptional.isEmpty() || !userValidationService.isPasswordMatch(userLoginDTO.getPassword(),
                userOptional.get().getPassword())) {
            throw new AuthenticationFailedException("Tài khoản hoặc mật khẩu không chính xác.");
        }

        User user = userOptional.get();
        validateEnabled(user); // Vẫn kiểm tra xem tài khoản có bị vô hiệu hóa không

        // Lấy danh sách roles của user
        List<String> roles = user.getAuthorities().stream()
                .map(Authority::getAuthority)
                .collect(Collectors.toList());

        // Tạo token với cả username và roles
        String rawToken = jwtUtil.generateToken(user.getUsername(), roles);
        String bearerToken = "Bearer " + rawToken; // Thêm Bearer prefix
        UserResponseDTO userReponseDTO = userMapper.converToDTO(user);

        // Trả về DTO chứa Bearer token, username và roles
        return new UserLoginResponseDTO(bearerToken, userReponseDTO);
    }

    @Override
    @Transactional
    public AdminLoginResponseDTO loginAdmin(AdminLoginDTO adminLoginDTO) {
        // Tìm user, nếu không tồn tại thì ném AuthenticationFailedException
        Optional<User> userOptional = userValidationService.findByUsername(adminLoginDTO.getUsername());
        if (userOptional.isEmpty()) {
            throw new AuthenticationFailedException("Username or password is incorrect");
        }

        User user = userOptional.get();

        // Validate password - nếu sai cũng ném AuthenticationFailedException
        try {
            validatePasswordInvalid(adminLoginDTO.getPassword(), user);
        } catch (IllegalArgumentException e) {
            throw new AuthenticationFailedException("Username or password is incorrect");
        }

        // Validate other conditions
        checkIsAdmin(user);
        validateEnabled(user);

        // Lấy danh sách roles của user
        List<String> roles = user.getAuthorities().stream()
                .map(Authority::getAuthority)
                .collect(Collectors.toList());

        // Tạo token với cả username và roles
        String rawToken = jwtUtil.generateToken(user.getUsername(), roles);
        String bearerToken = "Bearer " + rawToken; // Thêm Bearer prefix

        // Trả về DTO chứa Bearer token, username, message và roles
        return new AdminLoginResponseDTO(bearerToken, user.getUsername(), "Login successful", roles);
    }

    private void validateEnabled(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        if (!user.isEnabled()) {
            throw new IllegalArgumentException("Account is disabled");
        }
    }

    private void validatePasswordInvalid(String rawPassword, User user) {
        if (!userValidationService.isPasswordMatch(rawPassword, user.getPassword())) {
            throw new IllegalArgumentException("Invalid password");
        }
    }

    private void checkIsAdmin(User user) {
        boolean isAdmin = user.getAuthorities().stream()
                .anyMatch(
                        auth -> "ROLE_ADMIN".equals(auth.getAuthority()) || "ROLE_SYSTEM".equals(auth.getAuthority()));

        if (!isAdmin) {
            throw new IllegalArgumentException("Access denied. You are not an admin");
        }
    }

    @Override
    @Transactional
    public User register(UserRegisterDTO userRegisterDTO) {
        if (userValidationService.existsByUsername(userRegisterDTO.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (userValidationService.existsByEmail(userRegisterDTO.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        if (userValidationService.existsByPhone(userRegisterDTO.getPhone())) {
            throw new IllegalArgumentException("Phone already exists");
        }
        // Thêm xác thực dữ liệu
        validateUserRegisterDTO(userRegisterDTO);

        // Tạo quyền mặc định ROLE_USER
        Authority authority = new Authority();
        authority.setAuthority("ROLE_USER");
        authority.setCreatedAt(LocalDateTime.now());
        authority.setUpdatedAt(LocalDateTime.now());

        User user = userMapper.convertToEntity(
                userRegisterDTO,
                Set.of(authority),
                passwordEncoder.encode(userRegisterDTO.getPassword()));

        // Thiết lập quan hệ 2 chiều giữa User và Authority
        authority.setUser(user);

        // Lưu user và authority vào DB
        User savedUser = userRepository.save(user);
        authorityRepository.save(authority);

        return savedUser;
    }

    private void validateUserRegisterDTO(UserRegisterDTO userRegisterDTO) {
        if (!userRegisterDTO.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("Invalid email format");
        }
        if (userRegisterDTO.getPassword().length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters");
        }
    }
}
