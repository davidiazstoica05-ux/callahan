package com.callahan.callahancodigofuente.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Preferencias {

    @Id
    private Long id;


    @OneToOne
    @JoinColumn(name = "idUsuario")
    @MapsId
    private Usuario usuario;


    @ElementCollection
    private List<Integer> generosFavoritos;


    @ElementCollection
    private List<Integer> generosVetados;


    @Enumerated(EnumType.STRING)
    private EpocasPeliculas epocapelicula;

    private double toleranciaALaDuracion;

    @ElementCollection
    private Set<String> plataformasContratadas;

    private Long idDirectorFav;
    private Long idDirectorOdiado;


}
