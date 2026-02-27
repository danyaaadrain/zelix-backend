package ru.outofmemory.zelixbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.outofmemory.zelixbackend.dto.AuthRequestDTO;
import ru.outofmemory.zelixbackend.dto.AuthResponseDTO;
import ru.outofmemory.zelixbackend.dto.RegisterRequestDTO;
import ru.outofmemory.zelixbackend.entities.UserDetails;
import ru.outofmemory.zelixbackend.services.JwtService;
import ru.outofmemory.zelixbackend.services.UserService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserService userService;

    @PostMapping("/login")
    public AuthResponseDTO login(@RequestBody AuthRequestDTO request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );

            UserDetails userEntity = userService.findByUsername(request.getUsername()).orElseThrow();

            String token = jwtService.generateToken(userEntity, request.isRememberMe());
            return new AuthResponseDTO(token, userEntity.getUsername());

        } catch (AuthenticationException e) {
            throw new RuntimeException("Неверный логин или пароль");
        }
    }

    @PostMapping("/registration")
    public AuthResponseDTO register(@RequestBody RegisterRequestDTO request) {
        userService.findByUsername(request.getUsername()).ifPresent(user -> {
            throw new RuntimeException("Аккаунт с указанным логином уже создан");
        });
        userService.findByEmail(request.getEmail()).ifPresent(email -> {
            throw new RuntimeException("Аккаунт с указанной почтой уже создан");
        });
        UserDetails userEntity = new UserDetails(request.getUsername(), passwordEncoder.encode(request.getPassword()), request.getEmail());
        userService.saveUser(userEntity);
        String token = jwtService.generateToken(userEntity, false);
        return new AuthResponseDTO(token, userEntity.getUsername());
    }
}