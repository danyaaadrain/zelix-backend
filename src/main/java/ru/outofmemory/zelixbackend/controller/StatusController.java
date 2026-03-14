package ru.outofmemory.zelixbackend.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.outofmemory.zelixbackend.dto.status.StatusResponseDTO;
import ru.outofmemory.zelixbackend.entities.MinerEntity;
import ru.outofmemory.zelixbackend.entities.UserEntity;
import ru.outofmemory.zelixbackend.services.MinerService;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@RestController
@RequestMapping("/api/status")
@RequiredArgsConstructor
public class StatusController {
    private final MinerService minerService;

    @GetMapping("/devices")
    public StatusResponseDTO devices(@AuthenticationPrincipal UserEntity user) {
        StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
        List<MinerEntity> minerEntities = minerService.findAllMinersById(user.getId());
        minerEntities.forEach(minerEntity -> {
            statusResponseDTO.setTotalMiners(statusResponseDTO.getTotalMiners() + 1);
            if (minerEntity.getLastReport().isAfter(Instant.now().minus(1, ChronoUnit.MINUTES))) {
                statusResponseDTO.setOnlineMiners(statusResponseDTO.getOnlineMiners() + 1);
                statusResponseDTO.setTotalPowerConsumption(statusResponseDTO.getTotalPowerConsumption() + minerEntity.getPower());
                switch (minerEntity.getAlgo()) {
                    case "BTC":
                        statusResponseDTO.setBtcAvgHashrate(statusResponseDTO.getBtcAvgHashrate() + minerEntity.getRateAvg());
                        break;
                    case "LTC":
                        statusResponseDTO.setLtcAvgHashrate(statusResponseDTO.getLtcAvgHashrate() + minerEntity.getRateAvg());
                        break;
                }
            } else {
                statusResponseDTO.setOfflineMiners(statusResponseDTO.getOfflineMiners() + 1);
            }
        });

        return statusResponseDTO;
    }
}
