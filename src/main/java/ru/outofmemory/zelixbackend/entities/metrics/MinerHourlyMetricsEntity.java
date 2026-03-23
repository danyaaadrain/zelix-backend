package ru.outofmemory.zelixbackend.entities.metrics;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "hashrate_hourly_metrics")
public class MinerHourlyMetricsEntity extends BaseMinerMetricEntity {

}
