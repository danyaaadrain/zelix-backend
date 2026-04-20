package ru.outofmemory.zelixbackend.tasks;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.outofmemory.zelixbackend.entities.miner.TaskEntity;
import ru.outofmemory.zelixbackend.repos.TaskRepo;
import ru.outofmemory.zelixbackend.utilities.MinerTask;
import ru.outofmemory.zelixbackend.utilities.TaskStatus;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RebootTaskHandler implements ITaskHandler {

    private final TaskRepo taskRepo;

    @Override
    public MinerTask getType() {
        return MinerTask.REBOOT;
    }

    @Override
    public void handleTask(TasksWrapper tasksWrapper) {
        List<TaskEntity> taskEntities = tasksWrapper.getMiners().stream().map(miner -> {
            TaskEntity taskEntity = new TaskEntity();
            taskEntity.setMiner(miner);
            taskEntity.setMinerUuid(miner.getUuid());
            taskEntity.setMonitor(miner.getMonitor());
            taskEntity.setTask(tasksWrapper.getTask());
            taskEntity.setStatus(TaskStatus.CREATED);

            return taskEntity;
        }).toList();

        taskRepo.saveAll(taskEntities);
    }
}
