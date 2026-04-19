package ru.outofmemory.zelixbackend.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.outofmemory.zelixbackend.dto.ZelixMapper;
import ru.outofmemory.zelixbackend.dto.task.TaskRequestDto;
import ru.outofmemory.zelixbackend.dto.task.TaskResponseDto;
import ru.outofmemory.zelixbackend.entities.MonitorEntity;
import ru.outofmemory.zelixbackend.entities.UserEntity;
import ru.outofmemory.zelixbackend.entities.miner.MinerEntity;
import ru.outofmemory.zelixbackend.entities.miner.TaskEntity;
import ru.outofmemory.zelixbackend.repos.MinerRepo;
import ru.outofmemory.zelixbackend.repos.MonitorRepo;
import ru.outofmemory.zelixbackend.repos.TaskRepo;
import ru.outofmemory.zelixbackend.tasks.TaskRegistry;
import ru.outofmemory.zelixbackend.tasks.TasksWrapper;

import java.util.List;
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

    public void deleteCompletedTasks(MonitorEntity monitorEntity, List<Long> completedTasks) {
        taskRepo.deleteAllByMonitorIdAndIdIn(monitorEntity.getId(), completedTasks);
    }

    public List<TaskResponseDto> getTasksByMonitor(String apiToken, UUID monitorUuid) {
        UserEntity userEntity = userService.findUserByApiKey(apiToken);
        MonitorEntity monitorEntity = monitorRepo.findByIdAndOwnerId(monitorUuid, userEntity.getId()).orElseThrow(() ->
                new RuntimeException("Invalid monitor UUID")
        );
        List<TaskEntity> minerTaskEntities = taskRepo.findAllByMonitorId(monitorEntity.getId());

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
        List<MinerEntity> minerEntities = minerRepo.findAllByIdInAndOwnerId(taskRequestDto.getMinerIds(), userEntity.getId());
        TasksWrapper tasksWrapper = new TasksWrapper();
        tasksWrapper.setUser(userEntity);
        tasksWrapper.setMiners(minerEntities);
        tasksWrapper.setTask(taskRequestDto.getTask());
        tasksWrapper.setPayload(taskRequestDto.getPayload());
        taskRegistry.handleTask(tasksWrapper);
    }
}
