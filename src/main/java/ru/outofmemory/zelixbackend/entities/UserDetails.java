package ru.outofmemory.zelixbackend.entities;

import jakarta.persistence.*;
import lombok.*;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDetails implements org.springframework.security.core.userdetails.UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "username", nullable = false, length = 24, unique = true)
    private String username;

    @Column(name = "password", nullable = false, length = 128)
    private String password;

    @Column(name = "email", length = 64, unique = true)
    private String email;

    @Column(name = "api_key", length = 64, unique = true)
    private String apiKey;

    public UserDetails(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    @Override
    @NullMarked
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }
}
