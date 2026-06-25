package com.callahan.callahancodigofuente.controller;


import com.callahan.callahancodigofuente.dtos.FiltroEmocionesDTO;
import com.callahan.callahancodigofuente.dtos.Peliculas;
import com.callahan.callahancodigofuente.models.IntencionDiaria;
import com.callahan.callahancodigofuente.models.Usuario;
import com.callahan.callahancodigofuente.repository.UsuarioRepository;
import com.callahan.callahancodigofuente.service.EstadoAnimicoService;
import com.callahan.callahancodigofuente.service.TmdbService;
import com.callahan.callahancodigofuente.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

//Devuelve datos puros en JSON, no una pagina
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/callahan")
@CrossOrigin(origins = "*")
public class CallahanController {


    private final EstadoAnimicoService estadoAnimicoService;
    private final UsuarioService usuarioService;
    private  final TmdbService tmdbService;


    @PostMapping("/recomendar")
    public List<Peliculas> recibirDatosEmociones(@RequestBody FiltroEmocionesDTO peticion) {


        String generosEnString;


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

        generosEnString = transformarGenerosEnString(peticion);

        return tmdbService.obtenerPeliculasPorGenero(generosEnString);

    }




    //Metodo encapsulado
    private String transformarGenerosEnString(FiltroEmocionesDTO peticion){

        Set<IntencionDiaria> emociones = new HashSet<>(peticion.getEmociones());
        Set<Integer> generos = new HashSet<>();


        for ( IntencionDiaria emocion : emociones){

            generos.addAll(emocion.getGenerosAsociados());

        }

        return generos.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));


    }
}


