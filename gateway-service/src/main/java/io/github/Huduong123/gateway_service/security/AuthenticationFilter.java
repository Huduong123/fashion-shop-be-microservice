package io.github.Huduong123.gateway_service.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
public class AuthenticationFilter implements GlobalFilter, Ordered {

    @Autowired
    private JwtUtil jwtUtil;

    private final AntPathMatcher pathMatcher = new AntPathMatcher();
    // Danh sách các endpoint không cần xác thực
    private static final List<String> UNSECURED_ENDPOINTS = List.of(
            "/api/v1/user/auth/login",
            "/api/v1/user/auth/register",
            "/api/v1/user/auth/admin/login",
            "/api/v1/product/colors/**",
            "/api/v1/product/sizes/**",
            "/api/v1/product/categories/**",
            "/api/v1/product/products/**",
            "/api/v1/internal/**");

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();

        // Bỏ qua xác thực cho các endpoint public
        if (isUnsecured(request)) {
            return chain.filter(exchange);
        }

        // Kiểm tra header Authorization
        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return unauthorizedResponse(exchange, "Missing or invalid Authorization header");
        }

        String token = authHeader.substring(7);

        // Xác thực token
        if (!jwtUtil.validateToken(token)) {
            return unauthorizedResponse(exchange, "Invalid or expired JWT token");
        }

        // Lấy thông tin từ token và thêm vào header cho các service phía sau
        var claims = jwtUtil.getClaims(token);
        String username = claims.getSubject();
        Long userId = claims.get("userId", Long.class);
        List<String> roles = claims.get("authorities", List.class);

        ServerHttpRequest modifiedRequest = request.mutate()
                .header("X-Auth-Username", username)
                .header("X-Auth-User-Id", userId.toString())
                .header("X-Auth-Roles", String.join(",", roles)) // Chuyển list roles thành string
                .build();
        return chain.filter(exchange.mutate().request(modifiedRequest).build());
    }

    private boolean isUnsecured(ServerHttpRequest request) {
        String path = request.getURI().getPath();
        boolean isUnsecured = UNSECURED_ENDPOINTS.stream().anyMatch(uri -> pathMatcher.match(uri, path));

        // Debug logging để kiểm tra path matching
        System.out.println("DEBUG: Path: " + path + " | Is Unsecured: " + isUnsecured);
        if (!isUnsecured) {
            System.out.println("DEBUG: Path " + path + " requires authentication");
            UNSECURED_ENDPOINTS.forEach(
                    uri -> System.out.println("  - Pattern: " + uri + " | Matches: " + pathMatcher.match(uri, path)));
        }

        return isUnsecured;
    }

    private Mono<Void> unauthorizedResponse(ServerWebExchange exchange, String message) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        // Bạn có thể trả về một body JSON chi tiết hơn nếu muốn
        return exchange.getResponse().setComplete();
    }

    @Override
    public int getOrder() {
        return -1; // Chạy filter này trước các filter khác
    }
}