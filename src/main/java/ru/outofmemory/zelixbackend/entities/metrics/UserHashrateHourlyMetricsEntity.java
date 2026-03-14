package ru.outofmemory.zelixbackend.entities.metrics;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import ru.outofmemory.zelixbackend.entities.UserEntity;

import java.time.Instant;

@Entity
@Table(name = "user_hashrate_hourly_metrics")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserHashrateHourlyMetricsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @CreationTimestamp()
    @Column(name = "created_at")
    private Instant createdAt;

    private String algo;

    private double hashrate;
}
