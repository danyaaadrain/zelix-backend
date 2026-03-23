package ru.outofmemory.zelixbackend.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "pools")
@Getter
@Setter
@NoArgsConstructor
public class PoolEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "pool_id")
    private Integer poolId;
    private Integer priority;
    private String url;
    private String username;
    private String password;
    private String status;
    private Long accepted;
    private Long rejected;
    private Long stale;
    @Column(name = "last_share_time")
    private String lastShareTime;
    private Double diff;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "miner_id")
    private MinerEntity miner;
}