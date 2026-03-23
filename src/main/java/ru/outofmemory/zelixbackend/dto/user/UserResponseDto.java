package ru.outofmemory.zelixbackend.dto.user;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserResponseDto {
    private String username;
    private String email;
    private String apiKey;
}
