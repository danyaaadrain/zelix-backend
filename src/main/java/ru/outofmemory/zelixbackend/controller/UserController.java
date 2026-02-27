package ru.outofmemory.zelixbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.outofmemory.zelixbackend.entities.UserDetails;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    @GetMapping("/getuser")
    public ResponseEntity<UserDetails> getUser(@AuthenticationPrincipal UserDetails user) {
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
