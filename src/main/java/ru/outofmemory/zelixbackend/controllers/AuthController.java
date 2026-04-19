package ru.outofmemory.zelixbackend.controllers;

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
import ru.outofmemory.zelixbackend.dto.auth.AuthRequestDto;
import ru.outofmemory.zelixbackend.dto.auth.AuthResponseDto;
import ru.outofmemory.zelixbackend.dto.user.RegisterRequestDto;
import ru.outofmemory.zelixbackend.entities.UserEntity;
import ru.outofmemory.zelixbackend.exceptions.IncorrectCredentialsException;
import ru.outofmemory.zelixbackend.services.JwtService;
import ru.outofmemory.zelixbackend.services.UserService;
import ru.outofmemory.zelixbackend.utilities.Utilities;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserService userService;
    private final Utilities utilities;

    @PostMapping("/login")
    public AuthResponseDto login(@RequestBody AuthRequestDto request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );

            UserEntity userEntity = userService.findByUsername(request.getUsername()).orElseThrow();

            String token = jwtService.generateToken(userEntity, request.isRemember());
            return new AuthResponseDto(token, userEntity.getUsername());

        } catch (AuthenticationException e) {
            throw new IncorrectCredentialsException("Неверный логин или пароль");
        }
    }

    @PostMapping("/register")
    public AuthResponseDto register(@RequestBody RegisterRequestDto request) {
        userService.findByUsername(request.getUsername()).ifPresent(user -> {
            throw new RuntimeException("Аккаунт с указанным логином уже существует");
        });
        userService.findByEmail(request.getEmail()).ifPresent(email -> {
            throw new RuntimeException("Аккаунт с указанной почтой уже существует");
        });
        if (!utilities.isPasswordValid(request.getPassword())) {
            throw new RuntimeException("Пароль не удовлетворяет условиям");
        }
        UserEntity userEntity = new UserEntity(request.getUsername(), passwordEncoder.encode(request.getPassword()), request.getEmail());
        userService.saveUser(userEntity);
        String token = jwtService.generateToken(userEntity, false);
        return new AuthResponseDto(token, userEntity.getUsername());
    }
}