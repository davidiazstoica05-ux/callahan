package com.callahan.callahancodigofuente.models;

import java.util.List;

public enum IntencionDiaria {


    // 1. Estados para liberar energía y estrés
    DESCONECTAR_Y_REIR(List.of(35, 10770)),      // Comedia, Película de TV (ligeras)
    ADRENALINA_PURA(List.of(28, 12)),            // Acción, Aventura
    PASAR_MIEDO(List.of(27)),                    // Terror

    // 2. Estados para activar la cabeza
    TENSION_Y_MISTERIO(List.of(80, 9648, 53)),   // Crimen, Misterio, Suspense
    ESTIMULAR_LA_MENTE(List.of(878, 14)),        // Ciencia Ficción, Fantasía
    CULTURIZARME(List.of(99, 36)),               // Documental, Historia

    // 3. Estados emocionales y tranquilos
    LLORAR_A_MARES(List.of(18)),                 // Drama
    CREER_EN_EL_AMOR(List.of(10749)),            // Romance
    TARDE_CONFORTABLE(List.of(10751, 16)),       // Familia, Animación
    DISFRUTAR_MUSICA(List.of(10402)),            // Música

    // 4. Estados de nicho / Intensos
    EPICA_CLASICA(List.of(10752, 37));           // Guerra, Western

    // --- LÓGICA INTERNA DEL ENUM ---

    private final List<Integer> generosAsociados;

    IntencionDiaria(List<Integer> generosAsociados) {
        this.generosAsociados = generosAsociados;
    }

    public List<Integer> getGenerosAsociados() {
        return generosAsociados;
    }


}
