package com.callahan.callahancodigofuente.controller;

import com.callahan.callahancodigofuente.dtos.LoginRequestDTO;
import com.callahan.callahancodigofuente.models.Usuario;
import com.callahan.callahancodigofuente.repository.UsuarioRepository;
import com.callahan.callahancodigofuente.service.UsuarioService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/detectives")
@CrossOrigin(origins = "*")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final PasswordEncoder passwordEncoder;
    private final UsuarioRepository usuarioRepository;



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

        return new ResponseEntity<>(usuarioGuardado, HttpStatus.CREATED);
    }

    @PostMapping("/iniciarSesion")
    public ResponseEntity<Usuario> iniciarSesion(@RequestBody LoginRequestDTO datosInicioSesion, HttpServletRequest request) {

        //En lugar de hacerlo manualmente SpringSecurity se encarga de todo
        try {



            request.login(datosInicioSesion.getNombreUsuario(), datosInicioSesion.getPassw());

            Usuario usuarioLogueado = usuarioRepository.findByNombreUsuario(datosInicioSesion.getNombreUsuario())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado tras login"));

            return ResponseEntity.ok(usuarioLogueado);

        }catch (ServletException e) {
            Throwable causaReal = e.getCause(); // Desempaquetamos la caja

            if (causaReal instanceof UsernameNotFoundException) {
                System.out.println("El usuario no existe en la base de datos.");
            } else if (causaReal instanceof BadCredentialsException) {
                System.out.println("El usuario existe, pero la contraseña es incorrecta.");
            }

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}



