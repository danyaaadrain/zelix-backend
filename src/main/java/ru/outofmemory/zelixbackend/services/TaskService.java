package ru.outofmemory.zelixbackend.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.outofmemory.zelixbackend.dto.ZelixMapper;
import ru.outofmemory.zelixbackend.dto.task.TaskRequestDto;
import ru.outofmemory.zelixbackend.dto.task.TaskResponseDto;
import ru.outofmemory.zelixbackend.dto.task.TaskStatusDto;
import ru.outofmemory.zelixbackend.entities.MonitorEntity;
import ru.outofmemory.zelixbackend.entities.UserEntity;
import ru.outofmemory.zelixbackend.entities.miner.MinerEntity;
import ru.outofmemory.zelixbackend.entities.miner.TaskEntity;
import ru.outofmemory.zelixbackend.repos.MinerRepo;
import ru.outofmemory.zelixbackend.repos.MonitorRepo;
import ru.outofmemory.zelixbackend.repos.TaskRepo;
import ru.outofmemory.zelixbackend.tasks.TaskRegistry;
import ru.outofmemory.zelixbackend.tasks.TasksWrapper;
import ru.outofmemory.zelixbackend.utilities.MinerTask;
import ru.outofmemory.zelixbackend.utilities.TaskStatus;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepo taskRepo;
    private final MinerRepo minerRepo;
    private final MonitorRepo monitorRepo;

    private final UserService userService;
    private final ZelixMapper zelixMapper;
    private final TaskRegistry taskRegistry;

    public void updateTasks(MonitorEntity monitorEntity, List<TaskStatusDto> completedTasks) {
        if (completedTasks == null || completedTasks.isEmpty()) {
            return;
        }
        List<TaskEntity> taskEntities = taskRepo.findAllByMonitorId(monitorEntity.getId());
        taskEntities.forEach(taskEntity -> completedTasks.stream()
                .filter(task -> task.getId().equals(taskEntity.getId()))
                .findFirst()
                .ifPresent(completedTask -> taskEntity.setStatus(completedTask.getStatus()))
        );
        taskRepo.saveAll(taskEntities);
    }

    public List<TaskResponseDto> getCreatedTasksByMonitor(String apiToken, UUID monitorUuid) {
        UserEntity userEntity = userService.findUserByApiKey(apiToken);
        MonitorEntity monitorEntity = monitorRepo.findByIdAndOwnerId(monitorUuid, userEntity.getId()).orElseThrow(() ->
                new RuntimeException("Invalid monitor UUID")
        );
        List<TaskEntity> minerTaskEntities = taskRepo.findAllByMonitorIdAndStatus(monitorEntity.getId(), TaskStatus.CREATED);

        return minerTaskEntities.stream().map(zelixMapper::toTaskResponseDto).toList();
    }

    public List<TaskResponseDto> getTasksByMiner(UserEntity userEntity, Long id) {
        MinerEntity minerEntity = minerRepo.findByIdAndOwnerId(id, userEntity.getId()).orElseThrow(() ->
                new RuntimeException("Invalid miner Id")
        );

        List<TaskEntity> minerTaskEntities = taskRepo.findAllByMinerId(minerEntity.getId());
        return minerTaskEntities.stream().map(zelixMapper::toTaskResponseDto).toList();
    }

    public void createTasks(UserEntity userEntity, TaskRequestDto taskRequestDto) {
        List<Long> minerIds = taskRequestDto.getMinerIds();
        Set<Long> uniqueMinerIds = new HashSet<>(minerIds);
        if (uniqueMinerIds.size() != minerIds.size()) {
            throw new RuntimeException("Miner list contains duplicate ids");
        }

        validateTaskPayload(taskRequestDto);

        List<MinerEntity> minerEntities = minerRepo.findAllByIdInAndOwnerId(minerIds, userEntity.getId());
        if (minerEntities.size() != uniqueMinerIds.size()) {
            throw new RuntimeException("Some miners were not found or do not belong to the user");
        }

        TasksWrapper tasksWrapper = new TasksWrapper();
        tasksWrapper.setUser(userEntity);
        tasksWrapper.setMiners(minerEntities);
        tasksWrapper.setTask(taskRequestDto.getTask());
        tasksWrapper.setPayload(taskRequestDto.getPayload());
        taskRegistry.handleTask(tasksWrapper);
    }

    private void validateTaskPayload(TaskRequestDto taskRequestDto) {
        MinerTask task = taskRequestDto.getTask();
        Long payload = taskRequestDto.getPayload();

        switch (task) {
            case SET_POOLS -> {
                if (payload == null || payload <= 0) {
                    throw new RuntimeException("A valid template id is required to set pools");
                }
            }
            case SET_POWER_MODE -> {
                if (payload == null || payload < 0 || payload > 2) {
                    throw new RuntimeException("Power mode must be in the range from 0 to 2");
                }
            }
            case REBOOT, DELETE -> {
                if (payload != null) {
                    throw new RuntimeException("Payload is not required for the selected task");
                }
            }
            default -> throw new RuntimeException("Unsupported task type");
        }
    }
}
