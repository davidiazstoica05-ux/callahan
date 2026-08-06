package com.callahan.callahancodigofuente.service;


import com.callahan.callahancodigofuente.dtos.PeliculaDetalleDTO;
import com.callahan.callahancodigofuente.dtos.PeliculasDTO;
import com.callahan.callahancodigofuente.dtos.RespuestaTmdbDto;
import com.callahan.callahancodigofuente.models.PlataformasStreaming;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class TmdbService {

    @Value("${tmdb.api.key}")
    private String apiKey;

    @Value("${tmdb.api.url}")
    private String apiUrl;


    public List<PeliculasDTO> obtenerPeliculasPorGenero(String idGenero, boolean quiereAnime, Set<String> plataformasContratadas) {
        List<PeliculasDTO> peliculasTotales = new ArrayList<>();
        RestTemplate restTemplate = new RestTemplate();

        // Traducción para convertir el set en ids, desde un enum
        String proveedoresTraducidos = PlataformasStreaming.traducirConjuntoParaTmdb(plataformasContratadas);

        for (int i = 1; i <= 3; i++) {
            String urlFinal = apiUrl + "/discover/movie?api_key=" + apiKey
                    + "&with_genres=" + idGenero
                    + "&sort_by=vote_average.desc&vote_count.gte=3000"
                    + "&page=" + i;

            // 2. Solo añadimos la región y los proveedores si la traducción no está vacía
            if (!proveedoresTraducidos.isEmpty()) {
                urlFinal += "&watch_region=ES&with_watch_providers=" + proveedoresTraducidos;
            }

            RespuestaTmdbDto respuesta = restTemplate.getForObject(urlFinal, RespuestaTmdbDto.class);
            if (respuesta != null && respuesta.getResults() != null) {
                peliculasTotales.addAll(respuesta.getResults());
            }
        }

        //FiltroAnime
        if (!quiereAnime) {
            peliculasTotales.removeIf(peli -> "ja".equals(peli.getOriginalLanguage()));
        }

        return peliculasTotales;
    }

    public PeliculaDetalleDTO obtenerPorId(Long id) {


        String urlCompleta = apiUrl + "/movie/" + id + "?api_key=" + apiKey
                + "&language=es-ES&append_to_response=credits";

        RestTemplate restTemplate = new RestTemplate();

        PeliculaDetalleDTO respuesta = restTemplate.getForObject(urlCompleta, PeliculaDetalleDTO.class);

        return respuesta;

    }

    public String obtenerExpedienteCrudo(Long id) {
        String urlCompleta = apiUrl + "/movie/" + id + "?api_key=" + apiKey
                + "&language=es-ES&append_to_response=credits";
        RestTemplate restTemplate = new RestTemplate();

        // Fíjate que aquí le pedimos a Spring que nos lo devuelva como String.class
        return restTemplate.getForObject(urlCompleta, String.class);
    }


}
