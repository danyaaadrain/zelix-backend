package ru.outofmemory.zelixbackend.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.outofmemory.zelixbackend.entities.UserDetails;

import java.util.Optional;

public interface UserRepo extends JpaRepository<UserDetails, Long> {
    Optional<UserDetails> findByUsernameIgnoreCase(String username);
    Optional<UserDetails> findByEmailIgnoreCase(String email);
}
