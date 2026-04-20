package ru.outofmemory.zelixbackend.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ChangePasswordRequestDto {
    @NotBlank
    private String oldPassword;

    @JsonProperty("newPassword1")
    @NotBlank
    private String newPassword;
}
