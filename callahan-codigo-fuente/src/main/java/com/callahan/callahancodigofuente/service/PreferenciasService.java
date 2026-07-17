package com.callahan.callahancodigofuente.service;

import com.callahan.callahancodigofuente.dtos.Peliculas;
import com.callahan.callahancodigofuente.dtos.ProcesamientoPreferenciasDTO;
import com.callahan.callahancodigofuente.models.Preferencias;
import com.callahan.callahancodigofuente.repository.PreferenciasRepository;
import com.callahan.callahancodigofuente.repository.UsuarioRepository;
import com.callahan.callahancodigofuente.service.base.BaseService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.*;

@Service
public class PreferenciasService  extends BaseService <Preferencias, Long, PreferenciasRepository> {


    public List<Map.Entry<Integer, Integer>> procesarDatosCrudos(@RequestBody ProcesamientoPreferenciasDTO datosCrudos) {

        Map<Integer, Integer> diccionario = new HashMap<>();

        List<Integer> peliculasGustadas = datosCrudos.getPeliculasGustadas();
        List<Integer> peliculasNoGustadas = datosCrudos.getPeliculasNoGustadas();


        int puntosMeGusta = 2;
        int puntosDescarte = -1;



        for (int pelicula : peliculasGustadas) {
            if (diccionario.containsKey(pelicula)) {
                int puntajeActual = diccionario.get(pelicula);
                diccionario.put(pelicula, puntajeActual + puntosMeGusta);
            } else {
                diccionario.put(pelicula, puntosMeGusta);
            }
        }

        for (int pelicula : peliculasNoGustadas) {
            if (diccionario.containsKey(pelicula)) {
                int puntajeActual = diccionario.get(pelicula);
                diccionario.put(pelicula, puntajeActual + puntosDescarte);
            } else {
                diccionario.put(pelicula, puntosDescarte);
            }
        }

        List<Map.Entry<Integer, Integer>> entradasMap = new ArrayList<>(diccionario.entrySet());
        entradasMap.sort(Map.Entry.<Integer, Integer>comparingByValue().reversed());

        return entradasMap;
    }



}
