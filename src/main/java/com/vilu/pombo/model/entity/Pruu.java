package com.vilu.pombo.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vilu.pombo.model.dto.PruuDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table
public class Pruu {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @UuidGenerator
    private String uuid;

    @NotBlank(message = "É obrigatório inserir um texto.")
    @Size(min = 1, max = 300, message = "O pruu deve ter entre 1 e 300 caracteres.")
    private String texto;

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime dataHoraCriacao;

    @Column
    private int quantidadeLikes = 0;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    @JsonBackReference
    private Usuario usuario;

    @ManyToMany(mappedBy = "pruusCurtidos")
    @JsonIgnore
    private List<Usuario> usuariosQueCurtiram;

    @Column(columnDefinition = "LONGTEXT")
    private String imagemEmBase64;

    @Column
    private boolean bloqueado;

    @PrePersist
    protected void onCreate() {
        dataHoraCriacao = LocalDateTime.now();
        bloqueado = false;
    }

    public static PruuDTO toDTO(Pruu pruu, Integer qtdLikes, Integer qtdDenuncias) {
        if (!pruu.isBloqueado()) {
            pruu.setTexto("Bloqueado");
        }

        return new PruuDTO(pruu.getUuid(), pruu.getTexto(), pruu.getUsuario().getUuid(), pruu.getUsuario().getNome(), qtdLikes, qtdDenuncias);
    }

}