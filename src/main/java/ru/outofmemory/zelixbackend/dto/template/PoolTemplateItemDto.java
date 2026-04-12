package ru.outofmemory.zelixbackend.dto.template;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PoolTemplateItemDto {
    private String url;
    private String username;
    private String password;
}
