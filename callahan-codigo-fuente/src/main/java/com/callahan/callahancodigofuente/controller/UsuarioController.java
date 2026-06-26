package com.callahan.callahancodigofuente.controller;

import com.callahan.callahancodigofuente.models.Usuario;
import com.callahan.callahancodigofuente.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/detectives")
@CrossOrigin(origins = "*")
public class UsuarioController {

    private final UsuarioService usuarioService;


    @PostMapping("/registro")
    public ResponseEntity<Usuario> crearUsuario(@RequestBody Usuario nuevoUsuario) {


        Usuario usuarioGuardado = usuarioService.save(nuevoUsuario);

        //ResponseEntity actua como un optional pero entre el back y el frontend
        return new ResponseEntity<>(usuarioGuardado, HttpStatus.CREATED);
    }


}
