package ru.outofmemory.zelixbackend.tasks;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.outofmemory.zelixbackend.dto.ZelixMapper;
import ru.outofmemory.zelixbackend.dto.template.PoolTemplateDto;
import ru.outofmemory.zelixbackend.entities.miner.TaskEntity;
import ru.outofmemory.zelixbackend.entities.templates.PoolTemplateEntity;
import ru.outofmemory.zelixbackend.repos.TaskRepo;
import ru.outofmemory.zelixbackend.services.TemplateService;
import ru.outofmemory.zelixbackend.utilities.MinerTask;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SetPoolsTaskHandler implements ITaskHandler {
    private final TemplateService templateService;
    private final ZelixMapper zelixMapper;
    private final TaskRepo taskRepo;
    private final ObjectMapper objectMapper;

    @Override
    public MinerTask getType() {
        return MinerTask.SET_POOLS;
    }

    @Override
    public void handleTask(TasksWrapper tasksWrapper) {
        PoolTemplateEntity poolTemplateEntity = templateService.findByOwnerIdAndId(
                tasksWrapper.getUser().getId(),
                tasksWrapper.getPayload()
        );

        List<TaskEntity> taskEntities = tasksWrapper.getMiners().stream().map(miner -> {
            TaskEntity taskEntity = new TaskEntity();
            taskEntity.setMiner(miner);
            taskEntity.setMinerUuid(miner.getUuid());
            taskEntity.setMonitor(miner.getMonitor());
            taskEntity.setTask(tasksWrapper.getTask());

            PoolTemplateDto poolTemplateDto = zelixMapper.toPoolTemplateDto(poolTemplateEntity);
            String name;
            if (miner.getName() == null || miner.getName().isEmpty()) {
                name = String.valueOf(miner.getId());
            } else name = miner.getName();

            poolTemplateDto.getPools().forEach(pool -> pool.setUsername(pool.getUsername() + "." + name));
            taskEntity.setPayload(objectMapper.writeValueAsString(poolTemplateDto));

            return taskEntity;
        }).toList();

        taskRepo.saveAll(taskEntities);
    }
}
