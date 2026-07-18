package com.callahan.callahancodigofuente.service;

import com.callahan.callahancodigofuente.dtos.Peliculas;
import com.callahan.callahancodigofuente.dtos.ProcesamientoPreferenciasDTO;
import com.callahan.callahancodigofuente.models.EpocasPeliculas;
import com.callahan.callahancodigofuente.models.Preferencias;
import com.callahan.callahancodigofuente.repository.PreferenciasRepository;
import com.callahan.callahancodigofuente.repository.UsuarioRepository;
import com.callahan.callahancodigofuente.service.base.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;

@Service
@RequiredArgsConstructor
public class PreferenciasService  extends BaseService <Preferencias, Long, PreferenciasRepository> {

    private final UsuarioRepository usuarioRepository;

    public void procesarDatosCrudos(@RequestBody ProcesamientoPreferenciasDTO datosCrudos) {

        //Listas para luego construir el perfil de preferencias.++
        List<Integer> generosFavoritos = new ArrayList<>();
        List<Integer> generosDescartados = new ArrayList<>();

        List<Integer> directoresFavoritos = new ArrayList<>();
        List<Integer> directoresDescartados = new ArrayList<>();

        // Se cambian a Long (clase envoltorio) en lugar de int (primitivo)
        // para que puedan ser null si las listas vienen vacías, y coincidir con la entidad Preferencias.
        Long directorFav = null;
        Long directorOdiado = null;
        EpocasPeliculas epocaMasGustada = null;

        //Usaremos genericos al tener un enum
        //CalcularPuntuaciones
        List<Map.Entry<Integer, Integer>> rankingGeneros = calcularPuntuaciones(
                datosCrudos.getPeliculasGustadas(),
                datosCrudos.getPeliculasNoGustadas()
        );

        List<Map.Entry<Integer, Integer>> rankingDirectores = calcularPuntuaciones(
                datosCrudos.getDirectoresFav(),
                datosCrudos.getDirectoresOdiados()
        );

        List<Map.Entry<EpocasPeliculas,Integer>> rankingEpocas = calcularPuntuaciones(
                transformarAEnum(datosCrudos.getAniosGustados()),
                transformarAEnum(datosCrudos.getAniosDescartes())
        );

        //ExtraerResultados

        extraerResultados(rankingGeneros, generosFavoritos, generosDescartados);
        extraerResultados(rankingDirectores, directoresFavoritos, directoresDescartados);

        // Extracción de ganadores protegida y pidiendo el índice 0
        if (!rankingEpocas.isEmpty()) {
            epocaMasGustada = rankingEpocas.get(0).getKey();
        }

        if (!directoresFavoritos.isEmpty()) {
            directorFav = directoresFavoritos.get(0).longValue();
        }

        if (!directoresDescartados.isEmpty()) {
            directorOdiado = directoresDescartados.get(0).longValue();
        }


        Preferencias preferencias = Preferencias.builder().
                usuario(usuarioRepository.findUsuarioByIdUsuario(datosCrudos.getId()))
                .idDirectorFav(directorFav)
                .idDirectorOdiado(directorOdiado)
                .generosVetados(generosDescartados)
                .generosFavoritos(generosFavoritos)
                .toleranciaALaDuracion(null)
                .epocapelicula(epocaMasGustada)
                .plataformasContratadas(null)
                .build();

        this.save(preferencias);

    }

    //Motor del calculo
    //Genericos para poder reutilizar el metodo con el enum
    private <T> List<Map.Entry< T, Integer>> calcularPuntuaciones(List<T> elementosGustados,
                                                                  List<T> elementosDescartados) {
        Map<T, Integer> diccionario = new HashMap<>();
        int puntosMeGusta = 2;
        int puntosDescarte = -1;

        if (elementosGustados != null) {
            for (T elemento : elementosGustados) {
                diccionario.put(elemento, diccionario.getOrDefault(elemento, 0) + puntosMeGusta);
            }
        }

        if (elementosDescartados != null) {
            for (T elemento : elementosDescartados) {
                diccionario.put(elemento, diccionario.getOrDefault(elemento, 0) + puntosDescarte);
            }
        }

        List<Map.Entry<T, Integer>> entradasMap = new ArrayList<>(diccionario.entrySet());
        entradasMap.sort(Map.Entry.<T, Integer>comparingByValue().reversed());

        return entradasMap;
    }


    //Ordenar y extraer los resultados
    private <T> void extraerResultados(List<Map.Entry<T, Integer>> datosProcesados, List<T> resultadosFav,
                                       List<T> resultadosDesc) {
        if (!datosProcesados.isEmpty()) {
            int maximo = datosProcesados.get(0).getValue();
            int limiteVueltas = Math.min(3, datosProcesados.size());

            for (int i = 0; i < limiteVueltas; i++) {

                T elemento = datosProcesados.get(i).getKey();
                int puntaje = datosProcesados.get(i).getValue();

                if (puntaje > 0 && puntaje >= (maximo / 2)) {
                    resultadosFav.add(elemento);
                }
            }

            List<Map.Entry<T, Integer>> datosInvertidos = new ArrayList<>(datosProcesados);
            Collections.reverse(datosInvertidos);

            for (int i = 0; i < limiteVueltas; i++) {
                T elemento = datosInvertidos.get(i).getKey();
                int puntaje = datosInvertidos.get(i).getValue();

                if (puntaje < 0) {
                    resultadosDesc.add(elemento);
                }
            }
        }
    }


    private  List <EpocasPeliculas> transformarAEnum(List<String> aniosLanzamientos){

        List<EpocasPeliculas> epocasPeliculas = new ArrayList<>();

        if (aniosLanzamientos != null && !aniosLanzamientos.isEmpty()){

            for (String anio : aniosLanzamientos){

                int anioLanzamiento = Integer.parseInt(anio);

                EpocasPeliculas epocaPerteneciente = EpocasPeliculas.obtenerEpoca(anioLanzamiento);

                epocasPeliculas.add(epocaPerteneciente);

            }

        }

        return  epocasPeliculas;

    }

}