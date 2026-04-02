package ru.outofmemory.zelixbackend.entities.miner;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "chains")
@Getter
@Setter
@NoArgsConstructor
public class ChainEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "chain_id")
    private Integer chainId;
    @Column(name = "chip_count")
    private Integer chipCount;

    @Column(name = "chip_temp")
    private List<Integer> chipTemp;

    @Column(name = "chip_status")
    private String chipStatus;
    @Column(name = "pcb_temp_min")
    private Integer pcbMin;
    @Column(name = "pcb_temp_max")
    private Integer pcbMax;
    @Column(name = "pcb_hw_errors")
    private Long hwErrors;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "miner_id", nullable = false)
    private MinerEntity miner;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChainEntity that)) return false;
        return id != null && id.equals(that.getId());
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}