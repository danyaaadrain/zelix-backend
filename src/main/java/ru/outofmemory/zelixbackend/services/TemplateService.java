package ru.outofmemory.zelixbackend.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.outofmemory.zelixbackend.dto.ZelixMapper;
import ru.outofmemory.zelixbackend.dto.template.PoolTemplateDto;
import ru.outofmemory.zelixbackend.entities.UserEntity;
import ru.outofmemory.zelixbackend.entities.templates.PoolTemplateEntity;
import ru.outofmemory.zelixbackend.repos.PoolTemplateRepo;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TemplateService {
    private final PoolTemplateRepo poolTemplateRepo;
    private final ZelixMapper zelixMapper;

    public void createTemplate(UserEntity userEntity, PoolTemplateDto dto) {
        poolTemplateRepo.findByOwnerIdAndName(userEntity.getId(), dto.getName()).ifPresent(poolTemplate -> {
            throw new RuntimeException("Шаблон с указанным именем уже существует");
        });

        PoolTemplateEntity poolTemplateEntity = zelixMapper.toPoolTemplateEntity(dto);
        poolTemplateEntity.setOwner(userEntity);
        poolTemplateRepo.save(poolTemplateEntity);
    }

    public List<PoolTemplateDto> getTemplates(UserEntity user) {
        return zelixMapper.toPoolTemplateDto(poolTemplateRepo.findAllByOwnerId(user.getId()));
    }

    public void deleteTemplates(UserEntity userEntity, List<Long> ids) {
        poolTemplateRepo.deleteAllByOwnerIdAndIdIn(userEntity.getId(), ids);
    }
}
