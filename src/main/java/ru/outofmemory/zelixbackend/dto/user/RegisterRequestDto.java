package ru.outofmemory.zelixbackend.dto.user;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RegisterRequestDto {
    private String username;
    private String password;
    private String email;
}
