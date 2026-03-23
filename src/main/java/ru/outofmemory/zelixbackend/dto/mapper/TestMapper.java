package ru.outofmemory.zelixbackend.dto.mapper;

import org.mapstruct.*;
import ru.outofmemory.zelixbackend.dto.monitor.MinerDto;
import ru.outofmemory.zelixbackend.entities.MinerEntity;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface TestMapper {
    void updateMiner(MinerDto dto, @MappingTarget MinerEntity entity);

    @AfterMapping
    default void linkChains(@MappingTarget MinerEntity minerEntity) {
        minerEntity.getChains().forEach(chain -> chain.setMiner(minerEntity));
    }
    @AfterMapping
    default void linkPools(@MappingTarget MinerEntity minerEntity) {
        minerEntity.getPools().forEach(pool -> pool.setMiner(minerEntity));
    }
}
