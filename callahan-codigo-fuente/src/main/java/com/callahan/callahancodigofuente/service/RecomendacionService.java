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
        List<PeliculasDTO> recomendacionesFinal = new ArrayList<>();

        for (PeliculasDTO peli : peliculas) {
            boolean estaVetada = false;

            // 1. Solo comprobamos los géneros si el usuario realmente tiene géneros vetados guardados
            if (preferenciasUsuario.getGenerosVetados() != null && !preferenciasUsuario.getGenerosVetados().isEmpty()) {
                for (int idGenero : peli.getGenreIds()) {
                    for (int generoVetado : preferenciasUsuario.getGenerosVetados()) {
                        if (idGenero == generoVetado) {
                            estaVetada = true; // ¡Alerta! Contiene un género prohibido
                            break; // Rompemos el bucle porque ya sabemos que está vetada
                        }
                    }
                    if (estaVetada) break; // Salimos también del bucle exterior
                }
            }

            // 2. Si ha superado el filtro de géneros (o si no tenía géneros vetados), pasa al interrogatorio
            if (!estaVetada) {
                evaluarSuperviviente(peli, preferenciasUsuario, recomendacionesFinal);
            }
        }

        return recomendacionesFinal;
    }


    private void evaluarSuperviviente(PeliculasDTO superviviente, Preferencias preferenciasUsuario, List<PeliculasDTO> recomendacionesFinal) {
        EpocasPeliculas epocaPelicula = null;
        boolean directorValido = true;

        PeliculaDetalleDTO detalle = tmdbService.obtenerPorId(superviviente.getId());
        String fechaCompleta = detalle.getReleaseDate();
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

        Long idDirectorPeli = (director != null) ? director.getId() : null;

        // 1. Filtro estricto: Duración
        boolean duracionValida = duracion <= preferenciasUsuario.getToleranciaALaDuracion();

        // 2. Filtro estricto: Director odiado
        if (idDirectorPeli != null && preferenciasUsuario.getIdDirectorOdiado() != null) {
            if (idDirectorPeli.equals(preferenciasUsuario.getIdDirectorOdiado())) {
                directorValido = false;
            }
        }

        // 3. Veredicto y Recompensas (Puntos extra)
        if (duracionValida && directorValido) {

            // Evaluamos los puntos extra
            boolean esDirectorFav = idDirectorPeli != null && idDirectorPeli.equals(preferenciasUsuario.getIdDirectorFav());
            boolean esEpocaFav = preferenciasUsuario.getEpocapelicula() != null && epocaPelicula == preferenciasUsuario.getEpocapelicula();

            // Si es de su época favorita O de su director favorito, le damos prioridad absoluta
            if (esDirectorFav || esEpocaFav) {
                recomendacionesFinal.add(0, superviviente);
            } else {
                recomendacionesFinal.add(superviviente);
            }
        }
    }
}




