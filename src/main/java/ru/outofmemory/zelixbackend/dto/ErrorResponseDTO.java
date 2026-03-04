package ru.outofmemory.zelixbackend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ErrorResponseDTO {
    private String message;
    private int status;
}
