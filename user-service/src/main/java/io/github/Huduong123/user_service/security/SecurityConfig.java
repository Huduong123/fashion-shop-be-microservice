package io.github.Huduong123.user_service.security;


import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter,
            JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
    }

    private static final String[] PUBLIC_ENDPOINTS = {
            "/api/v1/user/auth/login",
            "/api/v1/user/auth/register",
            "/api/v1/user/auth/admin/login",
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Tắt CSRF vì chúng ta sử dụng JWT (stateless)
                .cors(Customizer.withDefaults()) // Kích hoạt CORS (sử dụng cấu hình từ bean corsConfigurationSource)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) 
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(PUBLIC_ENDPOINTS).permitAll()

                        // QUY TẮC 2: Áp dụng các quy tắc cụ thể cho các path con của /admin.
                        .requestMatchers("/api/v1/user/admin/accounts/**").hasRole("SYSTEM")
                        .requestMatchers("/api/v1/user/admin/payment-methods/**").hasAnyRole("ADMIN", "SYSTEM")
    
                        // QUY TẮC 3: Áp dụng quy tắc chung cho tất cả các path /admin còn lại.
                        .requestMatchers("/api/v1/user/admin/**").hasAnyRole("ADMIN", "SYSTEM")
    
                        // QUY TẮC 4: Các quy tắc cho user thường.
                        .requestMatchers("/api/v1/user/profile/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/api/v1/user/cart/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/api/v1/user/orders/**").hasRole("USER")
    
                        // QUY TẮC 5: Tất cả các request khác đều cần xác thực.
                        .anyRequest().authenticated()
                )
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint) // Xử lý các lỗi xác thực (ví dụ: thiếu
                                                                               // token, token không hợp lệ)

                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); // Thêm JWT filter trước filter mặc định của Spring Security
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)
            throws Exception {
        return configuration.getAuthenticationManager();
    }

    // Cấu hình CORS toàn cục
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Cần thay thế "*" bằng các origin cụ thể của frontend trong môi trường
        // production
        configuration.setAllowedOrigins(
                Arrays.asList("http://localhost:8080", "http://localhost:8081", "http://localhost:5174", "http://localhost:5173")); 
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")); 
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Accept")); 
        configuration.setAllowCredentials(true); // Cho phép gửi cookie hoặc HTTP authentication (nếu có)
        configuration.setMaxAge(3600L); // Thời gian cache cho pre-flight request

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Áp dụng cấu hình CORS này cho tất cả các đường dẫn
        return source;
    }
}
