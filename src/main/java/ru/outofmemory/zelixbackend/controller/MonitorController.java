package ru.outofmemory.zelixbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.outofmemory.zelixbackend.dto.MinerReportRequestDTO;
import ru.outofmemory.zelixbackend.dto.miner.MinerDTO;
import ru.outofmemory.zelixbackend.entities.UserEntity;
import ru.outofmemory.zelixbackend.repos.UserRepo;
import ru.outofmemory.zelixbackend.services.MinerService;

@RestController
@RequestMapping("/api/monitor")
@RequiredArgsConstructor
public class MonitorController {
    private final MinerService minerService;
    private final UserRepo userRepo;

    @PostMapping("/report")
    public ResponseEntity<?> report(@RequestBody MinerReportRequestDTO minersRequest) {
        UserEntity user = userRepo.findByApiKeyIgnoreCase(minersRequest.getApiKey()).orElseThrow(() ->
                new RuntimeException("Invalid api key")
        );
        if (minersRequest.getMiners() != null) {
            for (MinerDTO minerDto : minersRequest.getMiners()) {
                minerService.createOrUpdateMiner(minerDto, user);
            }
        }
        return ResponseEntity.ok().build();
    }
}
