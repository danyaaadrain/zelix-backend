package ru.outofmemory.zelixbackend.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.outofmemory.zelixbackend.dto.chart.ChartDto;
import ru.outofmemory.zelixbackend.entities.UserEntity;
import ru.outofmemory.zelixbackend.services.ChartService;
import ru.outofmemory.zelixbackend.utilities.ChartPeriod;
import ru.outofmemory.zelixbackend.utilities.MinerAlgo;

@RestController
@RequestMapping("/api/chart")
@RequiredArgsConstructor
public class ChartController {
    private final ChartService chartService;

    @GetMapping
    public ChartDto chart(@AuthenticationPrincipal UserEntity userEntity, @RequestParam MinerAlgo algo, @RequestParam ChartPeriod period) {
        return chartService.createMinersChart(userEntity, algo, period);
    }

    @GetMapping("/{id}")
    public ChartDto chartById(@AuthenticationPrincipal UserEntity userEntity, @PathVariable String id) {
        return new ChartDto();
    }
}
