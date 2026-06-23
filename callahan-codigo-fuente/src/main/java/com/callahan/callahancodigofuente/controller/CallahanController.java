package com.callahan.callahancodigofuente.controller;


import com.callahan.callahancodigofuente.dtos.FiltroEmocionesDTO;
import com.callahan.callahancodigofuente.models.IntencionDiaria;
import com.callahan.callahancodigofuente.models.Usuario;
import com.callahan.callahancodigofuente.repository.UsuarioRepository;
import com.callahan.callahancodigofuente.service.EstadoAnimicoService;
import com.callahan.callahancodigofuente.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

//Devuelve datos puros en JSON, no una pagina
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/callahan")
@CrossOrigin(origins = "*")
public class CallahanController {


    private final EstadoAnimicoService estadoAnimicoService;
    private final UsuarioRepository usuarioRepository;
    private final UsuarioService usuarioService;


    @PostMapping("/recomendar")
    public List<IntencionDiaria> recibirDatosEmociones(@RequestBody FiltroEmocionesDTO peticion) {

        Usuario usuarioActual = usuarioService.findById(1L).orElseGet(() -> {
            Usuario nuevoUsuario = Usuario.builder()
                    .nombreUsuario("detective_callahan")
                    .nombreReal("Harry")
                    .apellido1("Callahan")
                    .email("callahan@cinema.com")
                    .build();
            return usuarioService.save(nuevoUsuario);
        });

        estadoAnimicoService.agregarEstadoAnimico(peticion, usuarioActual);

        return peticion.getEmociones();
    }
}
