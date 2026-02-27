package ru.outofmemory.zelixbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AuthResponseDTO {
    private String token;
    private String username;
}
