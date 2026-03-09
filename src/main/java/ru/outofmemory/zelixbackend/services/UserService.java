package ru.outofmemory.zelixbackend.services;

import lombok.AllArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PasswordEncoder passwordEncoder;

    public void saveUser(UserEntity user) {
        userRepo.save(user);
    }

    public Optional<UserEntity> findByUsername(String username) {
        return userRepo.findByUsernameIgnoreCase(username);
    }

    public Optional<UserEntity> findByEmail(String email) {
        return userRepo.findByEmailIgnoreCase(email);
    }

    public void updateApiKey(UserEntity user, String apiKey) {
        user.setApiKey(apiKey);
        userRepo.save(user);
    }

    @Override
    @NullMarked
    public UserEntity loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Username not found: " + username));
    }

    public void changePassword(UserEntity user, String password) {
        user.setPassword(passwordEncoder.encode(password));
        user.setTokenVersion(user.getTokenVersion() + 1);
        userRepo.save(user);
    }
}
