package ru.outofmemory.zelixbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.outofmemory.zelixbackend.entities.MinerEntity;
import ru.outofmemory.zelixbackend.entities.UserEntity;
import ru.outofmemory.zelixbackend.services.MinerService;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {
    private final MinerService minerService;

    @GetMapping("/miners")
    public ResponseEntity<?> getMiners(@AuthenticationPrincipal UserEntity user) {
        ArrayList<MinerEntity> miners = minerService.findAllMinersById(user.getId());

        return new ResponseEntity<>(miners,HttpStatus.OK);
    }
}
