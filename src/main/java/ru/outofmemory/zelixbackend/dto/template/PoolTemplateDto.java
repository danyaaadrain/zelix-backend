package ru.outofmemory.zelixbackend.dto.template;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class PoolTemplateDto {
    private Long id;
    private String name;
    private List<PoolTemplateItemDto> pools;
}
