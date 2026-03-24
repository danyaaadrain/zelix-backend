package ru.outofmemory.zelixbackend.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.outofmemory.zelixbackend.entities.MinerEntity;
import ru.outofmemory.zelixbackend.utilities.MinerAlgo;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface MinerRepo extends JpaRepository<MinerEntity, Long> {
    ArrayList<MinerEntity> findAllByOwnerId(Long id);

    Integer countAllByOwnerId(Long ownerId);

    Integer countAllByOwnerIdAndLastReportIsAfter(Long ownerId, Instant afterTime);

    @Query("SELECT SUM(m.rate) FROM MinerEntity m " +
            "WHERE m.owner.id = :ownerId " +
            "AND m.algo = :algo " +
            "AND m.lastReport > :afterTime")
    Double sumRateByOwnerIdAndAlgoAndLastReportIsAfter(
            @Param("ownerId") Long ownerId,
            @Param("algo") MinerAlgo algo,
            @Param("afterTime") Instant afterTime
    );

    @Query("SELECT SUM(m.power) FROM MinerEntity m " +
            "WHERE m.owner.id = :ownerId " +
            "AND m.lastReport > :afterTime")
    Double sumPowerByOwnerIdAndLastReportIsAfter(
            @Param("ownerId") Long ownerId,
            @Param("afterTime") Instant afterTime
    );


    List<MinerEntity> findAllByMonitorIdAndOwnerId(UUID monitorId, Long ownerId);
    List<MinerEntity> findAllByUuidInAndOwnerId(List<UUID> uuid, Long ownerId);
    void deleteAllByUuidInAndOwnerId(Collection<UUID> uuids, Long ownerId);
}
