package ru.outofmemory.zelixbackend.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.outofmemory.zelixbackend.dto.miner.MinerCardDto;
import ru.outofmemory.zelixbackend.dto.miner.MinerDto;
import ru.outofmemory.zelixbackend.entities.UserEntity;
import ru.outofmemory.zelixbackend.services.MinerService;

import java.util.List;

@RestController
@RequestMapping("/api/miners")
@RequiredArgsConstructor
public class MinerController {
    private final MinerService minerService;

    @GetMapping
    public List<MinerCardDto> getMiners(@AuthenticationPrincipal UserEntity userEntity, @RequestParam(required = false) String q) {
        return minerService.getMinersCards(userEntity, q);
    }

    @GetMapping("/{id}")
    public MinerDto getMiner(@AuthenticationPrincipal UserEntity userEntity, @PathVariable Long id) {
        return minerService.getMiner(userEntity, id);
    }
}
