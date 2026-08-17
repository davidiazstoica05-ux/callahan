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


    @Column(nullable = false)
    private String rol;

    //Solo se puede tener un PrePersist por entidad
    @PrePersist
    private void onPrePersist() {
        // 1. Tu lógica original para la fecha
        setFechaRegistro(LocalDate.now());

        // 2. El blindaje de seguridad para el rol
        if (this.rol == null) {
            this.rol = "ROLE_USER";
        }
    }

}
