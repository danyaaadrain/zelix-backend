package ru.outofmemory.zelixbackend.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.outofmemory.zelixbackend.dto.mapper.MinerEntityMapper;
import ru.outofmemory.zelixbackend.dto.miner.MinerCardDto;
import ru.outofmemory.zelixbackend.dto.monitor.ChainDto;
import ru.outofmemory.zelixbackend.dto.monitor.MinerDto;
import ru.outofmemory.zelixbackend.dto.monitor.PoolDto;
import ru.outofmemory.zelixbackend.entities.*;
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
    private final MinerEntityMapper minerEntityMapper;

    public List<MinerCardDto> getMinersCards(UserEntity userEntity) {
        return minerRepo.findAllByOwnerId(userEntity.getId()).stream().map(minerEntity -> {
            MinerCardDto minerCardDto = minerEntityMapper.toMinerCardDto(minerEntity);
            minerCardDto.setOnline(minerEntity.getLastReport().isAfter(Instant.now().minus(1, ChronoUnit.MINUTES)));
            List<Integer> chipTemps = minerEntity.getChains().stream()
                    .sorted(Comparator.comparing(ChainEntity::getChainId))
                    .map(chainEntity -> chainEntity.getChipTemp().stream()
                            .max(Integer::compareTo).orElse(0)).toList();
            minerCardDto.setChipTemps(chipTemps);
            return minerCardDto;
        }).toList();
    }

    public void saveMiners(List<MinerDto> minersDto, UserEntity userEntity, MonitorEntity monitorEntity) {
        Map<UUID, MinerEntity> minerEntities = minerRepo.findAllByUuidInAndOwnerId(
                        minersDto.stream().map(MinerDto::getUuid).toList(),
                        userEntity.getId()).stream()
                .collect(Collectors.toMap(MinerEntity::getUuid, minerEntity -> minerEntity));
        minersDto.forEach(minerDto -> {
            MinerEntity minerEntity = minerEntities.get(minerDto.getUuid());
            if (minerEntity == null) {
                minerEntity = new MinerEntity();
                minerEntity.setOwner(userEntity);
                minerEntity.setMonitor(monitorEntity);
                minerEntities.put(minerDto.getUuid(), minerEntity);
            }
            minerEntityMapper.updateMiner(minerDto, minerEntity);
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
                    minerEntityMapper.updateChain(chainDto, chainEntity);
                } else {
                    chainEntity = minerEntityMapper.toChainEntity(chainDto);
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
                    minerEntityMapper.updatePool(poolDto, poolEntity);
                } else {
                    poolEntity = minerEntityMapper.toPoolEntity(poolDto);
                    poolEntity.setMiner(finalPoolMinerEntity);
                    finalPoolMinerEntity.getPools().add(poolEntity);
                }
            });
            minerEntity.getPools().removeIf(poolEntity -> !poolIds.contains(poolEntity.getPoolId()));
        });

        minerRepo.saveAll(minerEntities.values());
    }
}
