package ru.outofmemory.zelixbackend.entities.miner;

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
    @JoinColumn(name = "miner_id", nullable = false)
    private MinerEntity miner;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PoolEntity that)) return false;
        return id != null && id.equals(that.getId());
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}