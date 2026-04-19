package ru.outofmemory.zelixbackend.tasks;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.outofmemory.zelixbackend.entities.UserEntity;
import ru.outofmemory.zelixbackend.entities.miner.MinerEntity;
import ru.outofmemory.zelixbackend.utilities.MinerTask;

import java.util.List;

@Data
@NoArgsConstructor
public class TasksWrapper {
    private UserEntity user;
    private List<MinerEntity> miners;
    private MinerTask task;
    private Long payload;
}
