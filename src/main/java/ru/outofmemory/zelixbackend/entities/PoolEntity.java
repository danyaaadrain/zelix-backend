package ru.outofmemory.zelixbackend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "pools")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PoolEntity {
    @Id
    private String id;

    @Column(name = "pool_id")
    private int poolId;
    private int priority;
    private String url;
    private String username;
    private String password;
    private String status;
    private long accepted;
    private long rejected;
    private long stale;
    @Column(name = "last_share_time")
    private String lastShareTime;
    private double diff;
    @JsonIgnore // TODO удалить
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "miner_id")
    private MinerEntity miner;
}