package com.callahan.callahancodigofuente.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Interaccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Column(name = "pelicula_id", nullable = false)
    private Long peliculaId;

    @Column(nullable = false)
    private Integer puntuacion;

    @Column(name = "fecha_interaccion")
    private LocalDateTime fechaInteraccion;

    @PrePersist
    protected void onCreate() {
        this.fechaInteraccion = LocalDateTime.now();
    }

}
