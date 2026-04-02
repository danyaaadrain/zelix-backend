package ru.outofmemory.zelixbackend.entities.miner;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;
import ru.outofmemory.zelixbackend.entities.MonitorEntity;
import ru.outofmemory.zelixbackend.entities.UserEntity;
import ru.outofmemory.zelixbackend.entities.metrics.MinerDailyMetricsEntity;
import ru.outofmemory.zelixbackend.entities.metrics.MinerHourlyMetricsEntity;
import ru.outofmemory.zelixbackend.utilities.MinerAlgo;
import ru.outofmemory.zelixbackend.utilities.MinerType;

import java.time.Instant;
import java.util.*;

@Entity
@Table(name = "miners")
@Getter
@Setter
@NoArgsConstructor
public class MinerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private UUID uuid;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private UserEntity owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "monitor_id", nullable = false)
    private MonitorEntity monitor;

    @Enumerated(EnumType.STRING)
    private MinerAlgo algo;

    private String ip;
    private String mac;
    @Enumerated(EnumType.STRING)
    private MinerType type;
    private String sn;

    private Double rate;
    private Double rateAvg;
    private String rateUnit;
    private Integer power;
    private Long uptime;

    private List<Integer> fans;

    @UpdateTimestamp
    @Column(name = "last_report")
    private Instant lastReport;

    @OneToMany(mappedBy = "miner", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ChainEntity> chains = new HashSet<>();

    @OneToMany(mappedBy = "miner", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PoolEntity> pools = new HashSet<>();

    @OneToMany(mappedBy = "miner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MinerTaskEntity> tasks = new ArrayList<>();

    @OneToMany(mappedBy = "miner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MinerHourlyMetricsEntity> hourlyMetrics = new ArrayList<>();

    @OneToMany(mappedBy = "miner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MinerDailyMetricsEntity> dailyMetrics = new ArrayList<>();
}