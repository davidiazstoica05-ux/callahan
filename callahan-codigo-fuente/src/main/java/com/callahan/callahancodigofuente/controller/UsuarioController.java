package com.callahan.callahancodigofuente.controller;

import com.callahan.callahancodigofuente.models.Usuario;
import com.callahan.callahancodigofuente.service.UsuarioService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/detectives")
@CrossOrigin(origins = "*")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final PasswordEncoder passwordEncoder;


// Asegúrate de importar esto arriba:
// import jakarta.servlet.http.HttpServletRequest;
// import jakarta.servlet.ServletException;

    @PostMapping("/registro")
    public ResponseEntity<Usuario> crearUsuario(@RequestBody Usuario nuevoUsuario, HttpServletRequest request) { // Inyectamos el request

        //Cotraseña guardad en plano para usar luego el autologin
        String contrasenaPlana = nuevoUsuario.getPassw();

        nuevoUsuario.setRol("ROLE_USER");
        nuevoUsuario.setPassw(passwordEncoder.encode(contrasenaPlana));

        Usuario usuarioGuardado = usuarioService.save(nuevoUsuario);

        //AutoLogin
        try {

            request.login(usuarioGuardado.getNombreUsuario(), contrasenaPlana);
        } catch (ServletException e) {
            System.out.println("Error al auto-loguear al detective: " + e.getMessage());

            return new ResponseEntity<>(usuarioGuardado, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // 5. DEVOLVEMOS EL OK AL FRONT-END
        return new ResponseEntity<>(usuarioGuardado, HttpStatus.CREATED);
    }


}
