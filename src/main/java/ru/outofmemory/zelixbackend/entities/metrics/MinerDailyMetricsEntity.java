package ru.outofmemory.zelixbackend.entities.metrics;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "hashrate_daily_metrics")
public class MinerDailyMetricsEntity extends BaseMinerMetricEntity{

}
