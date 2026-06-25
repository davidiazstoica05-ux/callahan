package com.callahan.callahancodigofuente.service;


import com.callahan.callahancodigofuente.dtos.FiltroEmocionesDTO;
import com.callahan.callahancodigofuente.dtos.Peliculas;
import com.callahan.callahancodigofuente.dtos.RespuestaTmdbDto;
import com.callahan.callahancodigofuente.models.EstadoAnimico;
import com.callahan.callahancodigofuente.models.IntencionDiaria;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TmdbService {

    @Value("${tmdb.api.key}")
    private String apiKey;

    @Value("${tmdb.api.url}")
    private String apiUrl;


    public List<Peliculas> obtenerPeliculasPorGenero(String idGenero) {

        //El & sirve para decirle a la url que además quieres aplicar otra regla
        String urlFinal = apiUrl + "/discover/movie?api_key=" + apiKey + "&with_genres=" + idGenero + "&sort_by=vote_"
                + "average.desc&vote_count.gte=500";

        RestTemplate restTemplate = new RestTemplate();

        RespuestaTmdbDto respuesta = restTemplate.getForObject(urlFinal, RespuestaTmdbDto.class);

        return respuesta.getResults();

    }


}
