package ru.outofmemory.zelixbackend.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.outofmemory.zelixbackend.dto.miner.MinerCardDto;
import ru.outofmemory.zelixbackend.entities.UserEntity;
import ru.outofmemory.zelixbackend.services.MinerService;

import java.util.List;

@RestController
@RequestMapping("/api/miners")
@RequiredArgsConstructor
public class MinerController {
    private final MinerService minerService;

    @GetMapping
    public List<MinerCardDto> getMiners(@AuthenticationPrincipal UserEntity userEntity) {
        return minerService.getMinersCards(userEntity);
    }
}
