package ru.outofmemory.zelixbackend.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.outofmemory.zelixbackend.dto.miner.MinerCardDto;
import ru.outofmemory.zelixbackend.dto.miner.MinerDto;
import ru.outofmemory.zelixbackend.entities.UserEntity;
import ru.outofmemory.zelixbackend.services.MinerService;
import ru.outofmemory.zelixbackend.services.TaskService;

import java.util.List;

@RestController
@RequestMapping("/api/miners")
@RequiredArgsConstructor
public class MinerController {
    private final MinerService minerService;
    private final TaskService taskService;

    @GetMapping
    public List<MinerCardDto> getMiners(@AuthenticationPrincipal UserEntity userEntity, @RequestParam(required = false) String q) {
        return minerService.getMinersCards(userEntity, q);
    }

    @DeleteMapping
    public void deleteMiners(@AuthenticationPrincipal UserEntity userEntity, @RequestBody List<Long> ids) {
        taskService.createDeleteTask(userEntity, ids);
    }

    @GetMapping("/{id}")
    public MinerDto getMiner(@AuthenticationPrincipal UserEntity userEntity, @PathVariable Long id) {
        return minerService.getMiner(userEntity, id);
    }
}
