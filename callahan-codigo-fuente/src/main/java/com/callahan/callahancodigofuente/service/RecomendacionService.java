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

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class
RecomendacionService {

    // TODO: Eliminar la entidad EstadoAnimico, su repositorio y limpiar la base de datos (está generando problemas)
    
    private final TmdbService tmdbService;

    public List<PeliculasDTO> recomendarPeliculas(List<PeliculasDTO> peliculas, Preferencias preferenciasUsuario) {

        Map<PeliculasDTO, PeliculaDetalleDTO> supervivientes;
        Map<PeliculasDTO, Double> peliculasConPuntaje = new HashMap<>();

        double puntaje;


        //Parte 1 del algoritmo para descartar la basura
        supervivientes = descartarRapidamente(peliculas, preferenciasUsuario);
        PeliculaDetalleDTO detallePelicula;
        PeliculasDTO pelicula;


        //Parte 2 del algoritmo para clasificar
        for (Map.Entry<PeliculasDTO, PeliculaDetalleDTO> entry : supervivientes.entrySet()) {

            pelicula = entry.getKey();
            detallePelicula = entry.getValue();

            puntaje = calcularPuntuacion(pelicula, detallePelicula, preferenciasUsuario);

            peliculasConPuntaje.put(pelicula, puntaje);

        }

        List<PeliculasDTO> recomendacionesFinal = peliculasConPuntaje.entrySet().stream()
                .sorted(Map.Entry.<PeliculasDTO, Double>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());


        return recomendacionesFinal;
    }


    public Map<PeliculasDTO, PeliculaDetalleDTO> descartarRapidamente(List<PeliculasDTO> peliculas,
                                                                      Preferencias preferenciasUsuario) {

        // Usamos un mapa para salvar tanto la peli como su detalle de la API
        Map<PeliculasDTO, PeliculaDetalleDTO> supervivientes = new HashMap<>();

        for (PeliculasDTO peli : peliculas) {
            boolean estaVetada = false;

            // 1. Fase Géneros: Solo miramos si realmente hay géneros vetados
            if (preferenciasUsuario.getGenerosVetados() != null && !preferenciasUsuario.getGenerosVetados().isEmpty()) {
                for (int idGenero : peli.getGenreIds()) {
                    for (int generoVetado : preferenciasUsuario.getGenerosVetados()) {
                        if (idGenero == generoVetado) {
                            estaVetada = true;
                            break;
                        }
                    }
                    if (estaVetada) break;
                }
            }

            // 2. Fase Interrogatorio: Si superó los géneros (o no había vetos), la investigamos
            if (!estaVetada) {

                // Inocente hasta que se demuestre lo contrario
                boolean directorValido = true;

                // Descargamos el expediente COMPLETO (gastamos un recurso de la API)
                PeliculaDetalleDTO detalle = tmdbService.obtenerPorId(peli.getId());
                List<CrewDTO> crew = detalle.getCredits().getCrew();

                CrewDTO director = crew.stream()
                        .filter(p -> "Director".equals(p.getJob()))
                        .findFirst()
                        .orElse(null);

                Long idDirectorPeli = (director != null) ? director.getId() : null;

                // Filtro estricto: Director odiado
                if (idDirectorPeli != null && preferenciasUsuario.getIdDirectorOdiado() != null) {
                    if (idDirectorPeli.equals(preferenciasUsuario.getIdDirectorOdiado())) {
                        directorValido = false; // ¡Culpable!
                    }
                }

                // Si el director es válido, guardamos AMBAS cosas en el mapa
                if (directorValido) {
                    supervivientes.put(peli, detalle);
                }
            }
        }

        return supervivientes;
    }


    public double calcularPuntuacion(PeliculasDTO pelicula, PeliculaDetalleDTO detalleDTO, Preferencias
            preferenciasUsuario) {

        //El puntaje inicial comienza partiendo de su valoracion desde la API
        double puntaje = pelicula.getVoteAverage();

        // Puntos sumados por cada coincidencia
        double sumaDirectorFav = 15;
        double sumaEpocaFavorita = 5;
        double sumaGeneroFav = 10;
        double sumaDuracion = 10;

        String fechaLanzamiento = detalleDTO.getReleaseDate();

        int anioLanzamiento;
        EpocasPeliculas epocaPelicula;

        CrewDTO director;

        boolean tieneGeneroFav;

        double duracionPelicula = detalleDTO.getRuntime();
        double diferenciaTiempo;

        //Evaluacion de epoca
        if (fechaLanzamiento != null && fechaLanzamiento.length() >= 4) {
            //Parseo a int para que el enum sepatratarlo y le quitamos los guiones
            anioLanzamiento = Integer.parseInt(fechaLanzamiento.substring(0, 4));
            epocaPelicula = EpocasPeliculas.obtenerEpoca(anioLanzamiento);

            if (epocaPelicula == preferenciasUsuario.getEpocapelicula()) {
                puntaje += sumaEpocaFavorita;
            }
        }

        director = detalleDTO.getCredits().getCrew().stream()
                .filter(p -> "Director".equals(p.getJob()))
                .findFirst()
                .orElse(null);

        if (director != null && preferenciasUsuario.getIdDirectorFav() != null) {
            if (director.getId().equals(preferenciasUsuario.getIdDirectorFav())) {
                puntaje += sumaDirectorFav;
            }
        }

        if (preferenciasUsuario.getGenerosFavoritos() != null) {
            tieneGeneroFav = pelicula.getGenreIds().stream()
                    .anyMatch(idGenero -> preferenciasUsuario.getGenerosFavoritos().contains(idGenero));

            if (tieneGeneroFav) {
                puntaje += sumaGeneroFav;
            }
        }

        if (duracionPelicula <= preferenciasUsuario.getToleranciaALaDuracion()) {
            puntaje += sumaDuracion;
        } else {
            diferenciaTiempo = duracionPelicula - preferenciasUsuario.getToleranciaALaDuracion();
            puntaje -= diferenciaTiempo;
        }

        return puntaje;
    }

}




