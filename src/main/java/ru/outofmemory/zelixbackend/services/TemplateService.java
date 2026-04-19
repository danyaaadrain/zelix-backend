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
            throw new RuntimeException("Шаблон должен иметь от 1 до 3 пулов");
        }
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

    public PoolTemplateEntity findByOwnerIdAndId(Long ownerId, Long id) {
        return poolTemplateRepo.findByOwnerIdAndId(ownerId, id).orElseThrow(() ->
                new RuntimeException("Invalid template Id")
        );
    }
}
