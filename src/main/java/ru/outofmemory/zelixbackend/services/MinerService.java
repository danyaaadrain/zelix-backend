package ru.outofmemory.zelixbackend.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.outofmemory.zelixbackend.dto.ZelixMapper;
import ru.outofmemory.zelixbackend.dto.miner.ChainDto;
import ru.outofmemory.zelixbackend.dto.miner.MinerCardDto;
import ru.outofmemory.zelixbackend.dto.miner.MinerDto;
import ru.outofmemory.zelixbackend.dto.miner.PoolDto;
import ru.outofmemory.zelixbackend.entities.MonitorEntity;
import ru.outofmemory.zelixbackend.entities.UserEntity;
import ru.outofmemory.zelixbackend.entities.miner.ChainEntity;
import ru.outofmemory.zelixbackend.entities.miner.MinerEntity;
import ru.outofmemory.zelixbackend.entities.miner.PoolEntity;
import ru.outofmemory.zelixbackend.repos.MinerRepo;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Log4j2
@Service
@Transactional
@RequiredArgsConstructor
public class MinerService {
    private final MinerRepo minerRepo;
    private final ZelixMapper zelixMapper;

    public List<MinerCardDto> getMinersCards(UserEntity userEntity, String q) {
        List<MinerEntity> minerEntities;
        if (q != null) {
            Long id = null;
            try {
                id =  Long.parseLong(q);
            } catch (Exception ignored) {

            }
            minerEntities = minerRepo.searchByNameOrIdOrIp(userEntity.getId(), q, id);
        } else {
            minerEntities = minerRepo.findAllByOwnerId(userEntity.getId());
        }


        return minerEntities.stream().map(minerEntity -> {
            MinerCardDto minerCardDto = zelixMapper.toMinerCardDto(minerEntity);
            minerCardDto.setOnline(minerEntity.getLastReport().isAfter(Instant.now().minus(1, ChronoUnit.MINUTES)));
            List<Integer> chipTemps = minerEntity.getChains().stream()
                    .sorted(Comparator.comparing(ChainEntity::getChainId))
                    .map(chainEntity -> chainEntity.getChipTemp().stream()
                            .max(Integer::compareTo).orElse(0)).toList();
            minerCardDto.setChipTemps(chipTemps);
            return minerCardDto;
        }).toList();
    }

    public MinerDto getMiner(UserEntity userEntity, Long id) {
        MinerEntity minerEntity = minerRepo.findByIdAndOwnerId(id, userEntity.getId()).orElseThrow(() -> new RuntimeException("Miner with id=" + id + " not found"));
        MinerDto minerDto = zelixMapper.toMinerDto(minerEntity);
        minerDto.setOnline(minerEntity.getLastReport().isAfter(Instant.now().minus(1, ChronoUnit.MINUTES)));

        return minerDto;
    }

    public void saveMiners(List<MinerDto> minersDto, UserEntity userEntity, MonitorEntity monitorEntity) {
        Map<UUID, MinerEntity> minerEntities = minerRepo.findAllByMonitorIdAndOwnerId(monitorEntity.getId(), userEntity.getId())
                .stream()
                .collect(Collectors.toMap(MinerEntity::getUuid, minerEntity -> minerEntity));
        minersDto.forEach(minerDto -> {
            MinerEntity minerEntity = minerEntities.get(minerDto.getUuid());
            if (minerEntity == null) {
                minerEntity = new MinerEntity();
                minerEntity.setOwner(userEntity);
                minerEntity.setMonitor(monitorEntity);
                minerEntities.put(minerDto.getUuid(), minerEntity);
            }
            zelixMapper.updateMiner(minerDto, minerEntity);
            MonitorEntity minerMonitor = minerEntity.getMonitor();
            if (minerMonitor != null && minerMonitor.getId() != null && !minerMonitor.getId().equals(monitorEntity.getId())) {
                log.warn("User={} trying to update miner with uuid={} from monitor={}, but miner owned by monitor={}",
                        userEntity.getId(),
                        minerEntity.getUuid(),
                        monitorEntity.getId(),
                        minerMonitor.getId());
                return;
            }
            Map<Integer, ChainEntity> chainEntities = minerEntity.getChains().stream()
                    .collect(Collectors.toMap(ChainEntity::getChainId, c -> c));
            Set<Integer> chainIds = minerDto.getChains().stream().map(ChainDto::getChainId).collect(Collectors.toSet());
            MinerEntity finalChainMinerEntity = minerEntity;
            minerDto.getChains().forEach(chainDto -> {
                ChainEntity chainEntity = chainEntities.get(chainDto.getChainId());
                if (chainEntity != null) {
                    zelixMapper.updateChain(chainDto, chainEntity);
                } else {
                    chainEntity = zelixMapper.toChainEntity(chainDto);
                    chainEntity.setMiner(finalChainMinerEntity);
                    finalChainMinerEntity.getChains().add(chainEntity);
                }
            });
            minerEntity.getChains().removeIf(chainEntity -> !chainIds.contains(chainEntity.getChainId()));

            Map<Integer, PoolEntity> poolEntities = minerEntity.getPools().stream()
                    .collect(Collectors.toMap(PoolEntity::getPoolId, p -> p));
            Set<Integer> poolIds = minerDto.getPools().stream().map(PoolDto::getPoolId).collect(Collectors.toSet());
            MinerEntity finalPoolMinerEntity = minerEntity;
            minerDto.getPools().forEach(poolDto -> {
                PoolEntity poolEntity = poolEntities.get(poolDto.getPoolId());
                if (poolEntity != null) {
                    zelixMapper.updatePool(poolDto, poolEntity);
                } else {
                    poolEntity = zelixMapper.toPoolEntity(poolDto);
                    poolEntity.setMiner(finalPoolMinerEntity);
                    finalPoolMinerEntity.getPools().add(poolEntity);
                }
            });
            minerEntity.getPools().removeIf(poolEntity -> !poolIds.contains(poolEntity.getPoolId()));
        });
        List<UUID> uuids = minersDto.stream()
                .map(MinerDto::getUuid)
                .toList();
        List<UUID> toDelete = minerEntities.keySet().stream()
                .filter(uuid -> !uuids.contains(uuid))
                .toList();
        minerEntities.keySet().removeIf(uuid -> !uuids.contains(uuid));
        minerRepo.deleteAllByUuidInAndOwnerId(toDelete, userEntity.getId());
        minerRepo.saveAll(minerEntities.values());
    }
}
