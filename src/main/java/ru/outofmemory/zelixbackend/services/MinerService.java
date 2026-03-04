package ru.outofmemory.zelixbackend.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.outofmemory.zelixbackend.dto.miner.MinerDTO;
import ru.outofmemory.zelixbackend.entities.*;
import ru.outofmemory.zelixbackend.repos.MinerRepo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MinerService {
    private final MinerRepo minerRepo;

    public void saveMiners(List<MinerDTO> minersDto, UserEntity userEntity, MonitorEntity monitorEntity) {
        minersDto.forEach(minerDto -> {
            MinerEntity miner = minerRepo.findById(minerDto.getId()).orElseGet(MinerEntity::new);
            miner.setId(minerDto.getId());
            miner.setOwner(userEntity);
            miner.setMonitor(monitorEntity);

            miner.setIp(minerDto.getIp());
            miner.setMac(minerDto.getMac());
            miner.setType(minerDto.getType());
            miner.setSn(minerDto.getSn());
            miner.setRate(minerDto.getRate());
            miner.setRateAvg(minerDto.getRateAvg());
            miner.setPower(minerDto.getPower());
            miner.setUptime(minerDto.getUptime());

            miner.getChains().clear();
            miner.getPools().clear();

            minerDto.getChains().forEach(chainDto -> {
                ChainEntity chainEntity = new ChainEntity();
                chainEntity.setId(minerDto.getId() + "_" + chainDto.getId());
                chainEntity.setChainId(chainDto.getId());
                chainEntity.setChipCount(chainDto.getChipCount());
                chainEntity.setChipTemp(Arrays.stream(chainDto.getChipTemp()).boxed().toList());
                chainEntity.setChipStatus(chainDto.getChipStatus());
                chainEntity.setPcbMin(chainDto.getPcbMin());
                chainEntity.setPcbMax(chainDto.getPcbMax());
                chainEntity.setHwErrors(chainDto.getHwErrors());
                chainEntity.setMiner(miner);
                miner.getChains().add(chainEntity);
            });

            minerDto.getPools().forEach(poolDto -> {
                PoolEntity poolEntity = new PoolEntity();
                poolEntity.setId(minerDto.getId() + "_" + poolDto.getId());
                poolEntity.setPoolId(poolDto.getId());
                poolEntity.setPriority(poolDto.getPriority());
                poolEntity.setUrl(poolDto.getUrl());
                poolEntity.setUsername(poolDto.getUsername());
                poolEntity.setPassword(poolDto.getPassword());
                poolEntity.setStatus(poolDto.getStatus());
                poolEntity.setAccepted(poolDto.getAccepted());
                poolEntity.setRejected(poolDto.getRejected());
                poolEntity.setStale(poolDto.getStale());
                poolEntity.setLastShareTime(poolDto.getLast_share_time());
                poolEntity.setDiff(poolDto.getDiff());
                poolEntity.setMiner(miner);
                miner.getPools().add(poolEntity);
            });

            minerRepo.save(miner);
        });
    }

    public ArrayList<MinerEntity> findAllMinersById(Long id) {
        return minerRepo.findAllByOwnerId(id);
    }
}
