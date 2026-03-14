package ru.outofmemory.zelixbackend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;

@Entity
@Table(name = "chains")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChainEntity {

    @Id
    private String id;

    @Column(name = "chain_id")
    private int chainId;
    @Column(name = "chip_count")
    private int chipCount;

    @Column(name = "chip_temp", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<Integer> chipTemp;

    @Column(name = "chip_status")
    private String chipStatus;
    @Column(name = "pcb_temp_min")
    private int pcbMin;
    @Column(name = "pcb_temp_max")
    private int pcbMax;
    @Column(name = "pcb_hw_errors")
    private long hwErrors;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "miner_id")
    private MinerEntity miner;
}