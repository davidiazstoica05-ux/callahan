package com.callahan.callahancodigofuente.dtos;


import com.callahan.callahancodigofuente.models.IntencionDiaria;
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
public class FiltroEmocionesDTO {


    //Tiene que ser igual que la clavae del objeto que he creado en el .js para que Spring pueda mapearlo
    private List<IntencionDiaria> emociones;



}
