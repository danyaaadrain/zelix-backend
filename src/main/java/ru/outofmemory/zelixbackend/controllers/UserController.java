package ru.outofmemory.zelixbackend.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.outofmemory.zelixbackend.dto.auth.AuthResponseDto;
import ru.outofmemory.zelixbackend.dto.user.ChangePasswordRequestDto;
import ru.outofmemory.zelixbackend.dto.user.UserResponseDto;
import ru.outofmemory.zelixbackend.entities.UserEntity;
import ru.outofmemory.zelixbackend.utilities.Utilities;
import ru.outofmemory.zelixbackend.services.JwtService;
import ru.outofmemory.zelixbackend.services.UserService;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final Utilities apiKeyGenerator;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final Utilities utilities;


    @GetMapping("/me")
    public UserResponseDto getUser(@AuthenticationPrincipal UserEntity user) {
        UserResponseDto userResponseDTO = new UserResponseDto();
        userResponseDTO.setUsername(user.getUsername());
        userResponseDTO.setEmail(user.getEmail());
        userResponseDTO.setApiKey(user.getApiKey());
        return userResponseDTO;
    }

    @PatchMapping("/me/token")
    public UserResponseDto updateApiKey(@AuthenticationPrincipal UserEntity user) {
        userService.updateApiKey(user, apiKeyGenerator.createApiKey());
        UserResponseDto userResponseDTO = new UserResponseDto();
        userResponseDTO.setApiKey(user.getApiKey());
        return userResponseDTO;
    }

    @PatchMapping("/me/password")
    public AuthResponseDto password(@AuthenticationPrincipal UserEntity user, @RequestBody ChangePasswordRequestDto changePasswordRequestDTO) {
        if (changePasswordRequestDTO.getOldPassword().equals(changePasswordRequestDTO.getNewPassword())) {
            throw new RuntimeException("Новый и старый пароли совпадают");
        }
        if (!passwordEncoder.matches(changePasswordRequestDTO.getOldPassword(), user.getPassword())) {
            throw new RuntimeException("Неверно указан старый пароль");
        }
        if (!utilities.isPasswordValid(changePasswordRequestDTO.getNewPassword())) {
            throw new RuntimeException("Новый пароль не удовлетворяет условиям");
        }
        userService.changePassword(user, changePasswordRequestDTO.getNewPassword());
        AuthResponseDto authResponseDTO = new AuthResponseDto();
        authResponseDTO.setToken(jwtService.generateToken(user, false));
        return authResponseDTO;
    }
}
