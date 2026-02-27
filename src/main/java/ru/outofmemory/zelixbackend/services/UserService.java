package ru.outofmemory.zelixbackend.services;

import org.jspecify.annotations.NullMarked;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.outofmemory.zelixbackend.entities.UserDetails;
import ru.outofmemory.zelixbackend.repos.UserRepo;

import java.util.Optional;

@Service
@Transactional
public class UserService implements UserDetailsService {
    private final UserRepo userRepo;

    @Autowired
    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public void saveUser(UserDetails user) {
        userRepo.save(user);
    }

    public Optional<UserDetails> findByUsername(String username) {
        return userRepo.findByUsernameIgnoreCase(username);
    }

    public Optional<UserDetails> findByEmail(String email) {
        return userRepo.findByEmailIgnoreCase(email);
    }

    @Override
    @NullMarked
    public org.springframework.security.core.userdetails.UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Username not found: " + username));
    }
}
