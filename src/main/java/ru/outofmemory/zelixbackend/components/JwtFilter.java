package ru.outofmemory.zelixbackend.components;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.outofmemory.zelixbackend.dto.ErrorResponseDto;
import ru.outofmemory.zelixbackend.entities.UserEntity;
import ru.outofmemory.zelixbackend.services.JwtService;
import ru.outofmemory.zelixbackend.services.UserService;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserService userService;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String token = authHeader.substring(7);
            String username = jwtService.extractUsername(token);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserEntity user = userService.loadUserByUsername(username);
                if (!jwtService.isTokenValid(token, user)) {
                    throw new BadCredentialsException("Invalid token");
                }
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");

            ErrorResponseDto errorResponseDto = new ErrorResponseDto();
            errorResponseDto.setMessage(e.getMessage());
            errorResponseDto.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            objectMapper.writeValue(response.getOutputStream(), errorResponseDto);
            return;
        }

        filterChain.doFilter(request, response);
    }
}