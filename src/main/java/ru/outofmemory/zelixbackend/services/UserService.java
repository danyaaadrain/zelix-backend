package ru.outofmemory.zelixbackend.services;

import lombok.AllArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.outofmemory.zelixbackend.entities.UserEntity;
import ru.outofmemory.zelixbackend.repos.UserRepo;

import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepo userRepo;

    public void saveUser(UserEntity user) {
        userRepo.save(user);
    }

    public Optional<UserEntity> findByUsername(String username) {
        return userRepo.findByUsernameIgnoreCase(username);
    }

    public Optional<UserEntity> findByEmail(String email) {
        return userRepo.findByEmailIgnoreCase(email);
    }

    @Override
    @NullMarked
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Username not found: " + username));
    }
}
