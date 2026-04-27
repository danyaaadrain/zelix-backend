package ru.outofmemory.zelixbackend.controllers;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.outofmemory.zelixbackend.dto.template.PoolTemplateDto;
import ru.outofmemory.zelixbackend.entities.UserEntity;
import ru.outofmemory.zelixbackend.services.TemplateService;

import java.util.List;

@RestController
@RequestMapping("/api/templates")
@RequiredArgsConstructor
public class TemplateController {
    private final TemplateService templateService;

    @GetMapping
    public List<PoolTemplateDto> getTemplates(@AuthenticationPrincipal UserEntity user) {
        return templateService.getTemplates(user);
    }

    @PostMapping
    public void createTemplate(@AuthenticationPrincipal UserEntity user, @Valid @RequestBody PoolTemplateDto templateRequestDto) {
        templateService.createTemplate(user, templateRequestDto);
    }

    @DeleteMapping
    public void deleteTemplates(@AuthenticationPrincipal UserEntity user, @RequestBody List<Long> ids) {
        templateService.deleteTemplates(user, ids);
    }

    @PatchMapping
    public void editTemplate(@AuthenticationPrincipal UserEntity user, @RequestBody PoolTemplateDto templateRequestDto) {
        templateService.editTemplate(user, templateRequestDto);
    }
}
