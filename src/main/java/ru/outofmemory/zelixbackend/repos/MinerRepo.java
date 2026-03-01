package ru.outofmemory.zelixbackend.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.outofmemory.zelixbackend.entities.MinerEntity;
import ru.outofmemory.zelixbackend.entities.UserEntity;

import java.util.ArrayList;
import java.util.Optional;

public interface MinerRepo extends JpaRepository<MinerEntity, String> {
    ArrayList<MinerEntity> findAllByOwnerId(Long id);
}
