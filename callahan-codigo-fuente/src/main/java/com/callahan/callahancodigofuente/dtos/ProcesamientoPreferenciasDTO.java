package com.callahan.callahancodigofuente.dtos;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@Builder
@NoArgsConstructor
//Los DTOS se hacen para atrapar paquetes a media, al mandar un paquete de datos con una estructura que ninguna entidad
//la tiene el backend se vuelve loco y no sabra donde meter lo que le llega
public class ProcesamientoPreferenciasDTO {


    private Long id;
    private List<Integer> generosGustados;
    private List<Integer> generosNoGustados;
    private List<Integer> directoresOdiados;
    private List<Integer> directoresFav;
    private List<String> aniosGustados;
    private List<String> aniosDescartes;
    private List<Double> duracionPeliculasgustadas;
    private Set<String> plataformasContratadas;


}
