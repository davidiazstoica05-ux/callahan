package com.callahan.callahancodigofuente.service;

import com.callahan.callahancodigofuente.dtos.PeliculasDTO;
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


    public void recomendarPeliculas(List<PeliculasDTO> peliculas, Preferencias preferenciasUsuario) {

        List<PeliculasDTO> surpervivientes = new ArrayList<>();
        List<PeliculasDTO> recomendacionesFinale = new ArrayList<>();

        for (PeliculasDTO peli : peliculas) {

            for (int generoVetado : preferenciasUsuario.getGenerosVetados()) {


                for (int idGeneros : peli.getGenreIds()) {

                    if (idGeneros != generoVetado) {

                        surpervivientes.add(peli);
                    }


                }


            }


        }

        for (PeliculasDTO peliSupervivientes : surpervivientes) {

            //obtenerPorId devuelve un JSON, por lo tanto necesitamos traducirlo. Hay dos opciones: -Hacer diferetenesDTOS
            //O usar objectMapper que es un herramienta para arrancar directamente los datos
            ObjectMapper mapper = new ObjectMapper();

            try {
                String respuestaTMDb = tmdbService.obtenerPorId(peliSupervivientes.getId());
                // 3. Convertimos ese texto crudo en un "árbol" por el que podemos navegar
                JsonNode nodoRaiz = mapper.readTree(respuestaTMDb);

                // =========================================
                // EXTRACCIÓN 1: La duración (runtime)
                // =========================================
                // Es un dato directo en la raíz. Le pedimos que nos lo dé como número (int)
                // El '0' entre paréntesis es un salvavidas: si la película no tiene duración registrada, devolverá 0 en lugar de romper el programa.
                int duracion = nodoRaiz.path("runtime").asInt(0);


                // =========================================
                // EXTRACCIÓN 2: El Director
                // =========================================
                // El director es más escurridizo. Está dentro del nodo "credits", que a su vez tiene una lista llamada "crew".
                JsonNode equipoTecnico = nodoRaiz.path("credits").path("crew");

                Long idDirector = null;

                // Como "crew" es una lista (un array de JSON), tenemos que iterarla
                if (equipoTecnico.isArray()) {
                    for (JsonNode miembro : equipoTecnico) {
                        // Buscamos específicamente a la persona que tiene el trabajo de "Director"
                        if ("Director".equals(miembro.path("job").asText())) {
                            // Cuando lo encontramos, extraemos su ID y rompemos el bucle
                            idDirector = miembro.path("id").asLong();
                            break;
                        }
                    }
                }


            } catch (Exception e) {
                System.err.println("Error procesando el JSON profundo de la película: " + e.getMessage());
            }

        }


    }


}
