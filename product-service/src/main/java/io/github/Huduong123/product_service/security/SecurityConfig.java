package io.github.Huduong123.product_service.security;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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

        private final HeaderAuthenticationFilter headerAuthenticationFilter;

        // Inject filter vừa tạo
        public SecurityConfig(HeaderAuthenticationFilter headerAuthenticationFilter) {
                this.headerAuthenticationFilter = headerAuthenticationFilter;
        }

        // Chỉ định nghĩa các endpoint PUBLIC của CHÍNH service này
        private static final String[] PUBLIC_ENDPOINTS = {
                        "/api/v1/product/colors/**",
                        "/api/v1/product/sizes/**",
                        "/api/v1/product/categories/**",
                        "/api/v1/product/products/**",
                        "/uploads/**"
        };

        // Định nghĩa các endpoint chỉ ADMIN mới được truy cập
        private static final String[] ADMIN_ENDPOINTS = {
                        "/api/v1/product/admin/**"
        };

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                .csrf(csrf -> csrf.disable())
                                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                                .formLogin(formLogin -> formLogin.disable())
                                .httpBasic(httpBasic -> httpBasic.disable())
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                                                .requestMatchers(ADMIN_ENDPOINTS).hasAnyRole("ADMIN", "SYSTEM")
                                                .requestMatchers(PUBLIC_ENDPOINTS).permitAll()
                                                .anyRequest().authenticated());
                // Thêm filter của chúng ta vào trước filter mặc định
                http.addFilterBefore(headerAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
                return http.build();
        }

        @Bean
        public CorsConfigurationSource corsConfigurationSource() {
                CorsConfiguration configuration = new CorsConfiguration();
                configuration.setAllowedOrigins(Arrays.asList("http://localhost:8080", "http://localhost:8082",
                                "http://localhost:5173", "http://localhost:5174"));
                configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
                configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Accept"));
                configuration.setAllowCredentials(true);
                configuration.setMaxAge(3600L);

                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", configuration);
                return source;
        }
}