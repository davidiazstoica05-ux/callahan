package com.callahan.callahancodigofuente.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// ... (imports)
import jakarta.validation.constraints.NotBlank;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class LoginRequestDTO {

    @NotBlank(message = "El usuario no puede estar vacío")
    private String nombreUsuario;

    @NotBlank(message = "La contraseña no puede estar vacía")
    private String passw;
}
