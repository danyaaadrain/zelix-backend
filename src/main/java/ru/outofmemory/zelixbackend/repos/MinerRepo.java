package ru.outofmemory.zelixbackend.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.outofmemory.zelixbackend.entities.MinerEntity;

import java.util.ArrayList;

public interface MinerRepo extends JpaRepository<MinerEntity, String> {
    ArrayList<MinerEntity> findAllByOwnerId(Long id);
}
