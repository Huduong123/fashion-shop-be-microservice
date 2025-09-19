package io.github.Huduong123.order_service.security;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class HeaderAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String username = request.getHeader("X-Auth-Username");
        String userIdHeader = request.getHeader("X-Auth-User-Id");
        String rolesHeader = request.getHeader("X-Auth-Roles");

        if (username != null && !username.isEmpty() && userIdHeader != null && !userIdHeader.isEmpty()) {
            List<SimpleGrantedAuthority> authorities = rolesHeader == null || rolesHeader.isEmpty()
                    ? Collections.emptyList()
                    : Arrays.stream(rolesHeader.split(","))
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());

            // Use user ID as the principal name instead of username
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userIdHeader,
                    null,
                    authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }
}