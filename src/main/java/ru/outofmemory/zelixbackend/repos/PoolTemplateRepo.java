package ru.outofmemory.zelixbackend.repos;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.outofmemory.zelixbackend.entities.templates.PoolTemplateEntity;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface PoolTemplateRepo extends JpaRepository<PoolTemplateEntity, Long> {
    @EntityGraph(attributePaths = {"pools"})
    List<PoolTemplateEntity> findAllByOwnerId(Long ownerId);
    Optional<PoolTemplateEntity> findByOwnerIdAndName(Long ownerId, String name);

    void deleteAllByOwnerIdAndIdIn(Long ownerId, Collection<Long> ids);
}