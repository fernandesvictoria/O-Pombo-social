package com.victoria.pombo.model.entity;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Pruu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String uuid;

    @Column(nullable = false, length = 300)
    private String texto;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @Column(nullable = false)
    private int quantidadeLikes;

    @ManyToMany
    @JoinTable(
        name = "pruu_likes",
        joinColumns = @JoinColumn(name = "pruu_id"),
        inverseJoinColumns = @JoinColumn(name = "usuario_id")
    )

    @Column(nullable = false)
    private boolean bloqueado;

    
}
