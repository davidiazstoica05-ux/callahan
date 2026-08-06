package com.callahan.callahancodigofuente.controller;


import com.callahan.callahancodigofuente.dtos.FiltroEmocionesDTO;
import com.callahan.callahancodigofuente.dtos.PeliculaDetalleDTO;
import com.callahan.callahancodigofuente.dtos.PeliculasDTO;
import com.callahan.callahancodigofuente.models.IntencionDiaria;
import com.callahan.callahancodigofuente.models.Preferencias;
import com.callahan.callahancodigofuente.models.Usuario;
import com.callahan.callahancodigofuente.repository.EstadoAnimicoRepository;
import com.callahan.callahancodigofuente.repository.PreferenciasRepository;
import com.callahan.callahancodigofuente.repository.UsuarioRepository;
import com.callahan.callahancodigofuente.service.EstadoAnimicoService;
import com.callahan.callahancodigofuente.service.RecomendacionService;
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
    private final TmdbService tmdbService;
    private final UsuarioRepository usuarioRepository;
    private final RecomendacionService recomendacionService;
    private final PreferenciasRepository preferenciasRepository;

    @PostMapping("/recomendar")
    public List<PeliculasDTO> recibirDatosEmociones(@RequestBody FiltroEmocionesDTO peticion) {
        Long idUsuario = peticion.getIdUsuario();

        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Detective no encontrado con ID: " + idUsuario));

        Preferencias preferenciasUsuario = preferenciasRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Preferencias no encontradas para el detective: " + idUsuario));

        estadoAnimicoService.agregarEstadoAnimico(peticion, usuario);

        String generosEnString = transformarGenerosEnString(peticion);
        List<PeliculasDTO> peliculasPorGenero = tmdbService.obtenerPeliculasPorGenero(generosEnString,
                peticion.isAnime(), preferenciasUsuario.getPlataformasContratadas());

        return recomendacionService.recomendarPeliculas(peliculasPorGenero, preferenciasUsuario);
    }


    //Metodo encapsulado
    //Transforma los generos que llegan en map a string
    private String transformarGenerosEnString(FiltroEmocionesDTO peticion) {

        Set<IntencionDiaria> emociones = new HashSet<>(peticion.getEmociones());
        Set<Integer> generos = new HashSet<>();


        for (IntencionDiaria emocion : emociones) {

            generos.addAll(emocion.getGenerosAsociados());

        }

        return generos.stream()
                .map(String::valueOf)
                .collect(Collectors.joining("|"));


    }


    @GetMapping("/expedientes/{id}")
    @CrossOrigin
    // Ahora sí devolvemos el texto puro (JSON) al JavaScript
    public String obtenerPelicula(@PathVariable Long id) {
        return tmdbService.obtenerExpedienteCrudo(id);
    }


}


