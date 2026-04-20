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
        if (dto.getPools().isEmpty() || dto.getPools().size() > 3) {
            throw new RuntimeException("Template must contain from 1 to 3 pools");
        }
        poolTemplateRepo.findByOwnerIdAndName(userEntity.getId(), dto.getName()).ifPresent(poolTemplate -> {
            throw new RuntimeException("A template with this name already exists");
        });

        PoolTemplateEntity poolTemplateEntity = zelixMapper.toPoolTemplateEntity(dto);
        poolTemplateEntity.setOwner(userEntity);
        poolTemplateRepo.save(poolTemplateEntity);
    }

    public List<PoolTemplateDto> getTemplates(UserEntity user) {
        return zelixMapper.toPoolTemplateDto(poolTemplateRepo.findAllByOwnerId(user.getId()));
    }

    public void deleteTemplates(UserEntity userEntity, List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new RuntimeException("No templates were specified for deletion");
        }
        if (ids.stream().anyMatch(id -> id == null || id <= 0)) {
            throw new RuntimeException("Template list contains invalid ids");
        }
        poolTemplateRepo.deleteAllByOwnerIdAndIdIn(userEntity.getId(), ids);
    }

    public PoolTemplateEntity findByOwnerIdAndId(Long ownerId, Long id) {
        return poolTemplateRepo.findByOwnerIdAndId(ownerId, id).orElseThrow(() ->
                new RuntimeException("Invalid template Id")
        );
    }
}
