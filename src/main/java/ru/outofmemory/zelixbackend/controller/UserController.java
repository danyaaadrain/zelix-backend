package ru.outofmemory.zelixbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.outofmemory.zelixbackend.dto.auth.AuthResponseDTO;
import ru.outofmemory.zelixbackend.dto.user.ChangePasswordRequestDTO;
import ru.outofmemory.zelixbackend.dto.user.UserResponseDTO;
import ru.outofmemory.zelixbackend.entities.UserEntity;
import ru.outofmemory.zelixbackend.services.Utilities;
import ru.outofmemory.zelixbackend.services.JwtService;
import ru.outofmemory.zelixbackend.services.UserService;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final Utilities apiKeyGenerator;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final Utilities utilities;


    @GetMapping("/me")
    public UserResponseDTO getUser(@AuthenticationPrincipal UserEntity user) {
        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setUsername(user.getUsername());
        userResponseDTO.setEmail(user.getEmail());
        userResponseDTO.setApiKey(user.getApiKey());
        return userResponseDTO;
    }

    @PutMapping("/apikey")
    public UserResponseDTO updateApiKey(@AuthenticationPrincipal UserEntity user) {
        userService.updateApiKey(user, apiKeyGenerator.createApiKey());
        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setUsername(user.getUsername());
        userResponseDTO.setEmail(user.getEmail());
        userResponseDTO.setApiKey(user.getApiKey());
        return userResponseDTO;
    }

    @PatchMapping("/password")
    public AuthResponseDTO password(@AuthenticationPrincipal UserEntity user, @RequestBody ChangePasswordRequestDTO changePasswordRequestDTO) {
        if (changePasswordRequestDTO.getOldPassword().equals(changePasswordRequestDTO.getNewPassword())) {
            throw new RuntimeException("Новый и старый пароли совпадают");
        }
        if (!passwordEncoder.matches(changePasswordRequestDTO.getOldPassword(), user.getPassword())) {
            throw new RuntimeException("Неверно указан старый пароль");
        }
        if (!utilities.isPassowrdValid(changePasswordRequestDTO.getNewPassword())) {
            throw new RuntimeException("Новый пароль не удовлетворяет условиям");
        }
        userService.changePassword(user, changePasswordRequestDTO.getNewPassword());
        AuthResponseDTO authResponseDTO = new AuthResponseDTO();
        authResponseDTO.setToken(jwtService.generateToken(user, false));
        return authResponseDTO;
    }
}
