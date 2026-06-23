package com.callahan.callahancodigofuente.service;


import com.callahan.callahancodigofuente.dtos.Peliculas;
import com.callahan.callahancodigofuente.dtos.RespuestaTmdbDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class TmdbService {

    @Value("${tmdb.api.key}")
    private String apiKey;

    @Value("${tmdb.api.url}")
    private String apiUrl;


    public List<Peliculas> obtenerPeliculasPorGenero(String idGenero) {

        String urlFinal = apiUrl + "/discover/movie?api_key=" + apiKey + "&with_genres=" + idGenero;

        RestTemplate restTemplate = new RestTemplate();

        RespuestaTmdbDto respuesta = restTemplate.getForObject(urlFinal, RespuestaTmdbDto.class);

        return respuesta.getResults();

    }

}
