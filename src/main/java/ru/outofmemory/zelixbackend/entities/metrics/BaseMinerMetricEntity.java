package ru.outofmemory.zelixbackend.entities.metrics;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.outofmemory.zelixbackend.entities.UserEntity;
import ru.outofmemory.zelixbackend.entities.miner.MinerEntity;
import ru.outofmemory.zelixbackend.utilities.MinerAlgo;

import java.time.Instant;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
public class BaseMinerMetricEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "miner_id", nullable = false)
    private MinerEntity miner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private UserEntity owner;

    @Column(name = "created_at")
    private Instant createdAt;

    @Enumerated(EnumType.STRING)
    private MinerAlgo algo;
    private double hashrate;
    private int power;
}
