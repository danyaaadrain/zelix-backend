package ru.outofmemory.zelixbackend.dto.template;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class PoolTemplateDto {
    private Long id;

    @NotBlank
    private String name;

    @NotEmpty
    @Size(max = 3)
    private List<@NotNull @Valid PoolTemplateItemDto> pools;
}
