package com.callahan.callahancodigofuente.dtos;






import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@Builder
@NoArgsConstructor
//Los DTOS se hacen para atrapar paquetes a media, al mandar un paquete de datos con una estructura que ninguna entidad
//la tiene el backend se vuelve loco y no sabra donde meter lo que le llega
public class ProcesamientoPreferenciasDTO {

    List<Integer> peliculasGustadas;
    List<Integer> peliculasNoGustadas;
    List<Integer> directoresOdiados;
    List<Integer> directoresFav;



}
