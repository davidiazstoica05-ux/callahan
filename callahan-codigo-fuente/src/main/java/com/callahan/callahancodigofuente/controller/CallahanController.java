package com.callahan.callahancodigofuente.controller;


import com.callahan.callahancodigofuente.dtos.FiltroEmocionesDTO;
import com.callahan.callahancodigofuente.dtos.PeliculasDTO;
import com.callahan.callahancodigofuente.models.IntencionDiaria;
import com.callahan.callahancodigofuente.models.Usuario;
import com.callahan.callahancodigofuente.repository.UsuarioRepository;
import com.callahan.callahancodigofuente.service.EstadoAnimicoService;
import com.callahan.callahancodigofuente.service.TmdbService;
import com.callahan.callahancodigofuente.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.*;
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
    private final UsuarioRepository usuarioRepository;


    @PostMapping("/recomendar")
    public List<PeliculasDTO> recibirDatosEmociones(@RequestBody FiltroEmocionesDTO peticion) {


        String generosEnString;
        List<PeliculasDTO> peliculasPorGenero = new ArrayList<>();
        Long idUsuario = peticion.getIdUsuario();

        Optional<Usuario> usuarioActual= usuarioRepository.findById(idUsuario);



        estadoAnimicoService.agregarEstadoAnimico(peticion, usuarioActual.get());

        generosEnString = transformarGenerosEnString(peticion);

         peliculasPorGenero = tmdbService.obtenerPeliculasPorGenero(generosEnString);

    };




    //Metodo encapsulado
    //Transforma los generos que llegan en map a string
    private String transformarGenerosEnString(FiltroEmocionesDTO peticion){

        Set<IntencionDiaria> emociones = new HashSet<>(peticion.getEmociones());
        Set<Integer> generos = new HashSet<>();


        for ( IntencionDiaria emocion : emociones){

            generos.addAll(emocion.getGenerosAsociados());

        }

        return generos.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));


    };

    @GetMapping("/expedientes/{id}")
    @CrossOrigin
    public String obtenerPelicula(@PathVariable int id){

        return tmdbService.obtenerPorId(id);



    };

}


