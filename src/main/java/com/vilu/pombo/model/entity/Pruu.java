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

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public LocalDateTime getDataHoraCriacao() {
		return dataHoraCriacao;
	}

	public void setDataHoraCriacao(LocalDateTime dataHoraCriacao) {
		this.dataHoraCriacao = dataHoraCriacao;
	}

	public int getQuantidadeLikes() {
		return quantidadeLikes;
	}

	public void setQuantidadeLikes(int quantidadeLikes) {
		this.quantidadeLikes = quantidadeLikes;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public List<Usuario> getUsuariosQueCurtiram() {
		return usuariosQueCurtiram;
	}

	public void setUsuariosQueCurtiram(List<Usuario> usuariosQueCurtiram) {
		this.usuariosQueCurtiram = usuariosQueCurtiram;
	}

	public String getImagemEmBase64() {
		return imagemEmBase64;
	}

	public void setImagemEmBase64(String imagemEmBase64) {
		this.imagemEmBase64 = imagemEmBase64;
	}

	public boolean isBloqueado() {
		return bloqueado;
	}

	public void setBloqueado(boolean bloqueado) {
		this.bloqueado = bloqueado;
	}

}