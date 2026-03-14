package ru.outofmemory.zelixbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.outofmemory.zelixbackend.dto.charts.SummaryChartDTO;
import ru.outofmemory.zelixbackend.entities.UserEntity;
import ru.outofmemory.zelixbackend.services.ChartService;

@RestController
@RequestMapping("/api/chart")
@RequiredArgsConstructor
public class ChartController {
    private final ChartService chartService;

    @GetMapping
    public SummaryChartDTO chart(
            @AuthenticationPrincipal UserEntity userEntity,
            @RequestParam String algo,
            @RequestParam String period
    ) {
        return chartService.getSummaryChart(userEntity, algo);
    }
}
