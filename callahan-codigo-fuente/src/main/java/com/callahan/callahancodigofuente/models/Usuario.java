package com.callahan.callahancodigofuente.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDate;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUsuario;

    private String nombreUsuario;
    private String nombreReal;
    private String apellido1;
    private String apellido2;
    private String email;
    private String passw;
    private LocalDate fechaNacimiento;
    private String Pais;



}
