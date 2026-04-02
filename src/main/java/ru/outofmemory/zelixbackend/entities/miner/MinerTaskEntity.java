package ru.outofmemory.zelixbackend.entities.miner;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import ru.outofmemory.zelixbackend.entities.MonitorEntity;
import ru.outofmemory.zelixbackend.utilities.MinerTask;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "miner_tasks")
@Getter
@Setter
@NoArgsConstructor
public class MinerTaskEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "miner_id", nullable = false)
    private MinerEntity miner;

    @Column(name = "miner_uuid")
    private UUID minerUuid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "monitor_id", nullable = false)
    private MonitorEntity monitor;

    @CreationTimestamp
    @Column(name = "created_at")
    private Instant createdAt;

    @Enumerated(EnumType.STRING)
    private MinerTask task;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private String payload;
}
