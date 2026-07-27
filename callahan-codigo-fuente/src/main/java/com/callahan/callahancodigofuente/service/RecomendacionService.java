package com.callahan.callahancodigofuente.service;

import com.callahan.callahancodigofuente.dtos.CrewDTO;
import com.callahan.callahancodigofuente.dtos.PeliculaDetalleDTO;
import com.callahan.callahancodigofuente.dtos.PeliculasDTO;
import com.callahan.callahancodigofuente.models.EpocasPeliculas;
import com.callahan.callahancodigofuente.models.Preferencias;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class
RecomendacionService {


    private final TmdbService tmdbService;

    public List<PeliculasDTO> recomendarPeliculas(List<PeliculasDTO> peliculas, Preferencias preferenciasUsuario) {

        List<PeliculasDTO> surpervivientes = new ArrayList<>();
        List<PeliculasDTO> recomendacionesFinal = new ArrayList<>();

        List<CrewDTO> crew;

        PeliculaDetalleDTO detalle;

        String fechaCompleta;

        EpocasPeliculas epocaPelicula;

        int anioEstreno;
        int duracion;
        Long idDirectorPeli;

        CrewDTO director;

        boolean duracionValida;
        boolean directorValido;
        boolean epocaValida;


        for (PeliculasDTO peli : peliculas) {

            for (int generoVetado : preferenciasUsuario.getGenerosVetados()) {


                for (int idGeneros : peli.getGenreIds()) {

                    if (idGeneros != generoVetado) {

                        surpervivientes.add(peli);
                    }

                }


            }


            for (PeliculasDTO superviviente : surpervivientes) {

                evaluarSuperviviente(superviviente, preferenciasUsuario, recomendacionesFinal);

            }

        }

        return recomendacionesFinal;

    }


    //Hice toda esta parte yo solo y gemini se encargo de añadir seguridad y limpiar un poco el codigo
    private void evaluarSuperviviente(PeliculasDTO superviviente, Preferencias
            preferenciasUsuario, List<PeliculasDTO> recomendacionesFinal) {

        EpocasPeliculas epocaPelicula = null;
        boolean directorValido = true;
        boolean epocaValida = true;

        PeliculaDetalleDTO detalle = tmdbService.obtenerPorId(superviviente.getId());
        String fechaCompleta = detalle.getReleaseDate().getReleaseDate();
        int duracion = detalle.getRuntime();
        List<CrewDTO> crew = detalle.getCredits().getCrew();

        if (fechaCompleta != null && fechaCompleta.length() >= 4) {
            int anioEstreno = Integer.parseInt(fechaCompleta.substring(0, 4));
            epocaPelicula = EpocasPeliculas.obtenerEpoca(anioEstreno);
        }

        CrewDTO director = crew.stream()
                .filter(p -> "Director".equals(p.getJob()))
                .findFirst()
                .orElse(null);

        Long idDirectorPeli;
        if (director != null) {
            idDirectorPeli = director.getId();
        } else {
            idDirectorPeli = null;
        }

        boolean duracionValida = duracion <= preferenciasUsuario.getToleranciaALaDuracion();

        if (idDirectorPeli != null && preferenciasUsuario.getIdDirectorOdiado() != null) {
            if (idDirectorPeli.equals(preferenciasUsuario.getIdDirectorOdiado())) {
                directorValido = false;
            }
        }

        if (preferenciasUsuario.getEpocapelicula() != null && epocaPelicula != null) {
            if (epocaPelicula != preferenciasUsuario.getEpocapelicula()) {
                epocaValida = false;
            }
        }

        // Si pasa todas las pruebas, la añadimos a la lista que nos han pasado por parámetro
        if (duracionValida && directorValido && epocaValida) {
            if (idDirectorPeli != null && idDirectorPeli.equals(preferenciasUsuario.getIdDirectorFav())) {
                recomendacionesFinal.add(0, superviviente);
            } else {
                recomendacionesFinal.add(superviviente);
            }
        }
    }
}




