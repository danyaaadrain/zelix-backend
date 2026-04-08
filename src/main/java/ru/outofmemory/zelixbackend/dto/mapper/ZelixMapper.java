package ru.outofmemory.zelixbackend.dto.mapper;

import org.mapstruct.*;
import ru.outofmemory.zelixbackend.dto.miner.MinerCardDto;
import ru.outofmemory.zelixbackend.dto.miner.ChainDto;
import ru.outofmemory.zelixbackend.dto.miner.MinerDto;
import ru.outofmemory.zelixbackend.dto.miner.PoolDto;
import ru.outofmemory.zelixbackend.dto.task.TaskResponseDto;
import ru.outofmemory.zelixbackend.entities.miner.ChainEntity;
import ru.outofmemory.zelixbackend.entities.miner.MinerEntity;
import ru.outofmemory.zelixbackend.entities.miner.MinerTaskEntity;
import ru.outofmemory.zelixbackend.entities.miner.PoolEntity;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface ZelixMapper {
    ChainEntity toChainEntity(ChainDto chainDto);
    PoolEntity toPoolEntity(PoolDto poolDto);

    TaskResponseDto toTaskResponseDto(MinerTaskEntity minerTaskEntity);

    @Mapping(target = "type", source = "type.name")
    MinerCardDto toMinerCardDto(MinerEntity minerEntity);
    @Mapping(target = "type", source = "type.name")
    MinerDto toMinerDto(MinerEntity minerEntity);

    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "monitor", ignore = true)
    @Mapping(target = "chains", ignore = true)
    @Mapping(target = "pools", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", ignore = true)
    void updateMiner(MinerDto dto, @MappingTarget MinerEntity entity);
    void updateChain(ChainDto chainDto, @MappingTarget ChainEntity entity);
    void updatePool(PoolDto poolDto, @MappingTarget PoolEntity entity);

    @AfterMapping
    default void linkChains(@MappingTarget MinerEntity minerEntity) {
        minerEntity.getChains().forEach(chain -> chain.setMiner(minerEntity));
    }
    @AfterMapping
    default void linkPools(@MappingTarget MinerEntity minerEntity) {
        minerEntity.getPools().forEach(pool -> pool.setMiner(minerEntity));
    }
}