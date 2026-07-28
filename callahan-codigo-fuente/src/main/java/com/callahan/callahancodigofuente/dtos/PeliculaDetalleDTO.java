package com.callahan.callahancodigofuente.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class PeliculaDetalleDTO {

    private int runtime;
    private CreditsDTO credits;
    
    @JsonProperty("release_date")
    private String releaseDate;

}
