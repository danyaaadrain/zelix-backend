package ru.outofmemory.zelixbackend.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "monitors")
@Getter
@Setter
@NoArgsConstructor
public class MonitorEntity {
    @Id
    @Column(name = "id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private UserEntity owner;

    @UpdateTimestamp
    @Column(name = "last_report")
    private Instant lastReport;

    @Column(name = "monitor_ip")
    private String monitorIp;
    @Column(name = "monitor_mac")
    private String monitorMac;
    @Column(name = "uptime")
    private long uptime;
    @Column(name = "os_name")
    private String osName;
}
