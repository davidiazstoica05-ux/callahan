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


    public void procesarDatosCrudos(@RequestBody ProcesamientoPreferenciasDTO datosCrudos) {

        //Listas para luego construir el perfil de preferencias.
        List<Integer> generosFavoritos = new ArrayList<>();
        List<Integer> generosDescartados = new ArrayList<>();
        List<Integer> directoresFavoritos = new ArrayList<>();
        List<Integer> directoresDescartados = new ArrayList<>();

        //CalcularPuntuaciones
        List<Map.Entry<Integer, Integer>> rankingGeneros = calcularPuntuaciones(
                datosCrudos.getPeliculasGustadas(),
                datosCrudos.getPeliculasNoGustadas()
        );

        List<Map.Entry<Integer, Integer>> rankingDirectores = calcularPuntuaciones(
                datosCrudos.getDirectoresFav(),
                datosCrudos.getDirectoresOdiados()
        );


        //ExtraerResultados

        extraerResultados(rankingGeneros, generosFavoritos, generosDescartados);

        extraerResultados(rankingDirectores, directoresFavoritos, directoresDescartados);
    }

    private List<Map.Entry<Integer, Integer>> calcularPuntuaciones(List<Integer> elementosGustados,
                                                                   List<Integer> elementosDescartados) {
        Map<Integer, Integer> diccionario = new HashMap<>();
        int puntosMeGusta = 2;
        int puntosDescarte = -1;

        if (elementosGustados != null) {
            for (int elemento : elementosGustados) {
                diccionario.put(elemento, diccionario.getOrDefault(elemento, 0) + puntosMeGusta);
            }
        }

        if (elementosDescartados != null) {
            for (int elemento : elementosDescartados) {
                diccionario.put(elemento, diccionario.getOrDefault(elemento, 0) + puntosDescarte);
            }
        }

        List<Map.Entry<Integer, Integer>> entradasMap = new ArrayList<>(diccionario.entrySet());
        entradasMap.sort(Map.Entry.<Integer, Integer>comparingByValue().reversed());

        return entradasMap;
    }



    private void extraerResultados(List<Map.Entry<Integer, Integer>> datosProcesados, List<Integer> resultadosFav,
                                   List<Integer> resultadosDesc) {
        if (!datosProcesados.isEmpty()) {
            int maximo = datosProcesados.get(0).getValue();
            int limiteVueltas = Math.min(3, datosProcesados.size());

            for (int i = 0; i < limiteVueltas; i++) {
                int idElemento = datosProcesados.get(i).getKey();
                int puntaje = datosProcesados.get(i).getValue();

                if (puntaje > 0 && puntaje >= (maximo / 2)) {
                    resultadosFav.add(idElemento);
                }
            }

            List<Map.Entry<Integer, Integer>> datosInvertidos = new ArrayList<>(datosProcesados);
            Collections.reverse(datosInvertidos);

            for (int i = 0; i < limiteVueltas; i++) {
                int idElemento = datosInvertidos.get(i).getKey();
                int puntaje = datosInvertidos.get(i).getValue();

                if (puntaje < 0) {
                    resultadosDesc.add(idElemento);
                }
            }
        }
    }

}





