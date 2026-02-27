package ru.outofmemory.zelixbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ErrorResponseDTO {
    private String message;
    private int status;
}
