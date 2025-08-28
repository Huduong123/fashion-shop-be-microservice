package io.github.Huduong123.gateway_service.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity // <-- Sử dụng @EnableWebFluxSecurity cho Gateway (Reactive)
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
            // 1. Tắt CSRF cho Gateway
            .csrf(csrf -> csrf.disable()) 
            
            // 2. Tắt các cơ chế bảo mật mặc định khác như formLogin, httpBasic
            .formLogin(formLogin -> formLogin.disable())
            .httpBasic(httpBasic -> httpBasic.disable())
            
            // 3. Cho phép TẤT CẢ các request đi qua lớp bảo mật của Gateway
            // Lý do: Chúng ta sẽ dựa vào AuthenticationFilter (GlobalFilter)
            // để xử lý logic bảo mật một cách có chọn lọc.
            .authorizeExchange(auth -> auth
                .anyExchange().permitAll()
            );

        return http.build();
    }
}