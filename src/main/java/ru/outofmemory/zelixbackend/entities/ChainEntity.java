package ru.outofmemory.zelixbackend.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

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

    @Column(name = "chip_temp", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
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
    @JoinColumn(name = "miner_id")
    private MinerEntity miner;
}