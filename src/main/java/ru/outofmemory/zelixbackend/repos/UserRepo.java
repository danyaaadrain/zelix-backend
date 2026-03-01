package ru.outofmemory.zelixbackend.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.outofmemory.zelixbackend.entities.UserEntity;

import java.util.Optional;

public interface UserRepo extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsernameIgnoreCase(String username);
    Optional<UserEntity> findByEmailIgnoreCase(String email);
    Optional<UserEntity> findByApiKeyIgnoreCase(String apiKey);
}
