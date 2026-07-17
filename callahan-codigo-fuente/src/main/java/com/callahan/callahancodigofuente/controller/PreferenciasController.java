package com.callahan.callahancodigofuente.controller;

import com.callahan.callahancodigofuente.dtos.ProcesamientoPreferenciasDTO;
import com.callahan.callahancodigofuente.models.Preferencias;
import com.callahan.callahancodigofuente.service.PreferenciasService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/preferencias")
@CrossOrigin(origins = "*")
public class PreferenciasController {


    private final PreferenciasService preferenciasService;

    @PostMapping("/procesamientoDatos")
    public ResponseEntity<Void> procesarDatos(@RequestBody ProcesamientoPreferenciasDTO datosCrudos){

    List <Map.Entry<Integer, Integer>> datosTratados = preferenciasService.procesarDatosCrudos(datosCrudos);


        return ResponseEntity.ok().build();
    }



}
