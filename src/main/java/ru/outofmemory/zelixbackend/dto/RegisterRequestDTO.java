package ru.outofmemory.zelixbackend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RegisterRequestDTO {
    private String username;
    private String password;
    private String email;
}
