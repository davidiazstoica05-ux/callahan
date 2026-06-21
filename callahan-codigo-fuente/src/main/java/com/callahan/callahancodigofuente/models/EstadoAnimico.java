package com.callahan.callahancodigofuente.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EstadoAnimico {


    @Id
    private Long id;


    @ManyToOne
    @JoinColumn(name = "idUsuario")
    @MapsId
    private Usuario usuario;

    @Enumerated(EnumType.STRING)
    private IntencionDiaria intencionDiaria;

    LocalDate fecha;

}
