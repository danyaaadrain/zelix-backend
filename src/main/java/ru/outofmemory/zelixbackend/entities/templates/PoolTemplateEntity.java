package ru.outofmemory.zelixbackend.entities.templates;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.outofmemory.zelixbackend.entities.UserEntity;

import java.util.List;

@Entity
@Table(name = "templates")
@Getter
@Setter
@NoArgsConstructor
public class PoolTemplateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private UserEntity owner;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "template", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PoolTemplateItemEntity> pools;
}
