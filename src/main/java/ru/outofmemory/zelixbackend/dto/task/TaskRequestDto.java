package ru.outofmemory.zelixbackend.dto.task;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.outofmemory.zelixbackend.utilities.MinerTask;

import java.util.List;

@Data
@NoArgsConstructor
public class TaskRequestDto {
    @NotEmpty
    private List<@NotNull @Positive Long> minerIds;

    @NotNull
    private MinerTask task;

    private Long payload;
}
