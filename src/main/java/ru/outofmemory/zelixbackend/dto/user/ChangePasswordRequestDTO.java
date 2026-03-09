package ru.outofmemory.zelixbackend.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ChangePasswordRequestDTO {
    private String oldPassword;
    @JsonProperty("newPassword1")
    private String newPassword;
}
