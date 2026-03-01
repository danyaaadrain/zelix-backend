package ru.outofmemory.zelixbackend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "miners")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MinerEntity {
    @Id
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private UserEntity owner;

    private String ip;
    private String mac;
    private String type;
    private String sn;

    private double rate;
    private double rateAvg;
    private int power;
    private long uptime;

    @OneToMany(mappedBy = "miner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChainEntity> chains = new ArrayList<>();

    @OneToMany(mappedBy = "miner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PoolEntity> pools = new ArrayList<>();

    // геттеры/сеттеры
}