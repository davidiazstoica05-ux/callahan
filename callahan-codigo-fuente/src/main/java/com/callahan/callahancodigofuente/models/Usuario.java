package com.callahan.callahancodigofuente.models;

import jakarta.persistence.*;
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
    private String email;
    private String passw;
    private LocalDate fechaNacimiento;
    private String pais;

    private LocalDate fechaRegistro;

    @Builder.Default
    @Column(nullable = false)
    private String rol = "ROLE_USER";

    @PrePersist
    private void actualizarFechaRegistro() {

        setFechaRegistro(LocalDate.now());


    }


}
