package com.callahan.callahancodigofuente.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.AssertTrue;
import java.time.temporal.ChronoUnit;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUsuario;

    @NotBlank(message = "El nombre de usuario no puede estar en blanco")
    @Size(min = 3, max = 20, message = "El usuario debe tener entre 3 y 20 caracteres")
    private String nombreUsuario;

    @NotBlank(message = "El nombre real es obligatorio")
    private String nombreReal;

    @NotBlank(message = "El apellido es obligatorio")
    private String apellido1;

    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "El formato del email no es válido")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String passw;

    @NotNull(message = "La fecha de nacimiento es obligatoria")
    @Past(message = "La fecha de nacimiento debe ser en el pasado")
    private LocalDate fechaNacimiento;

    @NotBlank(message = "El país es obligatorio")
    private String pais;

    private LocalDate fechaRegistro;


    @Column(nullable = false)
    private String rol;


   //Pequeños metodos
    //AssertTrue se ejecuta cuando un metodo tiene colocado el @valid (En este caso en UsuarioController)
    @AssertTrue(message = "El detective debe tener entre 10 y 100 años de edad.")
    private boolean isFechaNacimientoValida() {
        if (fechaNacimiento == null) {
            return true;
        }

        long edad = ChronoUnit.YEARS.between(fechaNacimiento, LocalDate.now());
        return edad >= 10 && edad <= 100;
    }

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
