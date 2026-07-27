package com.callahan.callahancodigofuente.service;


import com.callahan.callahancodigofuente.dtos.PeliculaDetalleDTO;
import com.callahan.callahancodigofuente.dtos.PeliculasDTO;
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


    public List<PeliculasDTO> obtenerPeliculasPorGenero(String idGenero) {

        //El & sirve para decirle a la url que además quieres aplicar otra regla
        String urlFinal = apiUrl + "/discover/movie?api_key=" + apiKey + "&with_genres=" + idGenero + "&sort_by=vote_"
                + "average.desc&vote_count.gte=500";

        RestTemplate restTemplate = new RestTemplate();

        RespuestaTmdbDto respuesta = restTemplate.getForObject(urlFinal, RespuestaTmdbDto.class);

        return respuesta.getResults();

    }

    public PeliculaDetalleDTO obtenerPorId(Long id) {


        String urlCompleta = apiUrl + "/movie/" + id + "?api_key=" + apiKey + "&language=es-ES&append_to_response=credits";

        RestTemplate restTemplate = new RestTemplate();

        PeliculaDetalleDTO respuesta = restTemplate.getForObject(urlCompleta, PeliculaDetalleDTO.class);

        return respuesta;

    }

    public String obtenerExpedienteCrudo(Long id) {
        String urlCompleta = apiUrl + "/movie/" + id + "?api_key=" + apiKey + "&language=es-ES&append_to_response=credits";
        RestTemplate restTemplate = new RestTemplate();

        // Fíjate que aquí le pedimos a Spring que nos lo devuelva como String.class
        return restTemplate.getForObject(urlCompleta, String.class);
    }


}
