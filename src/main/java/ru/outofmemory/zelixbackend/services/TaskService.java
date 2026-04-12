package ru.outofmemory.zelixbackend.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.outofmemory.zelixbackend.dto.ZelixMapper;
import ru.outofmemory.zelixbackend.dto.task.TaskResponseDto;
import ru.outofmemory.zelixbackend.entities.MonitorEntity;
import ru.outofmemory.zelixbackend.entities.UserEntity;
import ru.outofmemory.zelixbackend.entities.miner.MinerEntity;
import ru.outofmemory.zelixbackend.entities.miner.MinerTaskEntity;
import ru.outofmemory.zelixbackend.repos.MinerRepo;
import ru.outofmemory.zelixbackend.repos.MinerTaskRepo;
import ru.outofmemory.zelixbackend.repos.MonitorRepo;
import ru.outofmemory.zelixbackend.utilities.MinerTask;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class TaskService {
    private final MinerTaskRepo minerTaskRepo;
    private final MinerRepo minerRepo;
    private final MonitorRepo monitorRepo;
    private final UserService userService;
    private final ZelixMapper zelixMapper;

    public List<TaskResponseDto> getTasks(String apiToken, UUID monitorUuid) {
        UserEntity userEntity = userService.findUserByApiKey(apiToken);
        MonitorEntity monitorEntity = monitorRepo.findByIdAndOwnerId(monitorUuid, userEntity.getId()).orElseThrow(() ->
                new RuntimeException("Invalid monitor UUID")
        );
        List<MinerTaskEntity> minerTaskEntities = minerTaskRepo.findAllByMonitorId(monitorEntity.getId());

        return minerTaskEntities.stream().map(zelixMapper::toTaskResponseDto).toList();
    }

    public void createDeleteTask(UserEntity userEntity, List<Long> minersIds) {
        List<MinerEntity> minerEntities = minerRepo.findAllByIdInAndOwnerId(minersIds, userEntity.getId());
        List<MinerTaskEntity> minerTaskEntities = minerEntities.stream().map(miner -> {
            MinerTaskEntity minerTaskEntity = new MinerTaskEntity();
            minerTaskEntity.setMiner(miner);
            minerTaskEntity.setMinerUuid(miner.getUuid());
            minerTaskEntity.setMonitor(miner.getMonitor());
            minerTaskEntity.setTask(MinerTask.DELETE);

            return minerTaskEntity;
        }).toList();

        minerTaskRepo.saveAll(minerTaskEntities);
    }

}
