package com.callahan.callahancodigofuente.service;


import com.callahan.callahancodigofuente.dtos.FiltroEmocionesDTO;
import com.callahan.callahancodigofuente.models.EstadoAnimico;
import com.callahan.callahancodigofuente.models.Usuario;
import com.callahan.callahancodigofuente.repository.EstadoAnimicoRepository;
import com.callahan.callahancodigofuente.service.base.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EstadoAnimicoService extends BaseService<EstadoAnimico, Long, EstadoAnimicoRepository> {


    public void agregarEstadoAnimico(@RequestBody FiltroEmocionesDTO peticion, Usuario usuario) {


        List<EstadoAnimico> emociones = new ArrayList<>();

        peticion.getEmociones().forEach(e -> {

            EstadoAnimico nuevoEstado = EstadoAnimico.builder()
                    .fecha(LocalDate.now())
                    .intencionDiaria(e)
                    .usuario(usuario)
                    .anime(peticion.isAnime())
                    .build();
        });
        this.repo.saveAll(emociones);
    }

}
