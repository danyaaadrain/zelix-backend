package ru.outofmemory.zelixbackend.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuthRequestDto {
    @NotBlank
    private String username;

    @NotBlank
    private String password;

    private boolean remember;
}
