package ru.outofmemory.zelixbackend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuthRequestDTO {
    private String username;
    private String password;
    private boolean rememberMe;
}
