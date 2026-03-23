package ru.outofmemory.zelixbackend.dto.auth;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuthRequestDto {
    private String username;
    private String password;
    private boolean rememberMe;
}
