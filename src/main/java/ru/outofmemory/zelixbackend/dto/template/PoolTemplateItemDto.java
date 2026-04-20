package ru.outofmemory.zelixbackend.dto.template;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PoolTemplateItemDto {
    @NotBlank
    private String url;

    @NotBlank
    private String username;

    private String password;
}
