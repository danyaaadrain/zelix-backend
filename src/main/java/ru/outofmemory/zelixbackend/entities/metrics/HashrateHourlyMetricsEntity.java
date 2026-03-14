package ru.outofmemory.zelixbackend.entities.metrics;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import ru.outofmemory.zelixbackend.entities.MinerEntity;

import java.time.Instant;

@Entity
@Table(name = "hashrate_hourly_metrics")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HashrateHourlyMetricsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "miner_id")
    private MinerEntity miner;

    @CreationTimestamp()
    @Column(name = "created_at")
    private Instant createdAt;

    private double hashrate;
}
