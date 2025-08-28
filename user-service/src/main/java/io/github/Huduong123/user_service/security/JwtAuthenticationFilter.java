package io.github.Huduong123.user_service.security;

import java.io.IOException;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.github.Huduong123.user_service.service.auth.UserAuthService;
import io.github.Huduong123.user_service.service.common.UserValidationService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final UserAuthService userAuthService;
    private final UserValidationService userValidationService;

    @Autowired
    public JwtAuthenticationFilter(JwtUtil jwtUtil, UserAuthService userAuthService,
            UserValidationService userValidationService) {
        this.jwtUtil = jwtUtil;
        this.userAuthService = userAuthService;
        this.userValidationService = userValidationService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        String username = null;
        String jwt = null;

        if (authHeader != null && !authHeader.trim().isEmpty()) {
            // Only accept Bearer format - no backward compatibility
            if (authHeader.startsWith("Bearer ")) {
                jwt = authHeader.substring(7);
                    username = jwtUtil.extractUsername(jwt);
            }
        } 

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            var userOpt = userValidationService.findByUsername(username);
            if (userOpt.isPresent()) {
                var user = userOpt.get();
                if (jwtUtil.validateToken(jwt)) {
                    // Spring Security expects GrantedAuthority objects
                    var authorities = user.getAuthorities().stream()
                            .map(auth -> new org.springframework.security.core.authority.SimpleGrantedAuthority(
                                    auth.getAuthority()))
                            .collect(Collectors.toList());

                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            user, null, authorities);
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } 
        }
        filterChain.doFilter(request, response);
    }
}
