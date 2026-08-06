package com.callahan.callahancodigofuente.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public enum PlataformasStreaming {

    // --- LOS GIGANTES GLOBALES ---
    NETFLIX("8"),
    PRIME_VIDEO("119"),
    MAX("384"), // Anteriormente HBO Max
    DISNEY_PLUS("337"),
    APPLE_TV("350"),
    PARAMOUNT_PLUS("531"),
    SKYSHOWTIME("1773"),

    // --- PLATAFORMAS NACIONALES (ESPAÑA) ---
    FILMIN("63"),
    MOVISTAR_PLUS("149"),
    ATRES_PLAYER("315"),
    RTVE_PLAY("354"),
    MITELE("393"),

    // --- PLATAFORMAS DE NICHO Y ANIME ---
    CRUNCHYROLL("283"),
    MUBI("11"), // Cine independiente y de autor
    DAZN("454"), // Deportes y documentales deportivos

    // --- PLATAFORMAS GRATUITAS CON ANUNCIOS (FAST) ---
    PLUTO_TV("300"),
    RAKUTEN_TV("35"),
    TUBI("73");

    private final String idTmdb;

    // Constructor del Enum
    PlataformasStreaming(String idTmdb) {
        this.idTmdb = idTmdb;
    }

    public String getIdTmdb() {
        return idTmdb;
    }

    /**
     * MÉTODO ENCAPSULADO
     * Recibe el Set de nombres ("NETFLIX", "MAX") y devuelve "8|384"
     */
    public static String traducirConjuntoParaTmdb(Set<String> plataformasFrontEnd) {
        // Escudo de seguridad por si viene vacío
        if (plataformasFrontEnd == null || plataformasFrontEnd.isEmpty()) {
            return "";
        }

        List<String> idsTraducidos = new ArrayList<>();

        for (String nombrePlataforma : plataformasFrontEnd) {
            try {
                // valueOf() busca automáticamente en el Enum el elemento que coincida
                PlataformasStreaming plataformaEnum = PlataformasStreaming.valueOf(nombrePlataforma.toUpperCase());

                // Si lo encuentra, sacamos su número y lo guardamos
                idsTraducidos.add(plataformaEnum.getIdTmdb());

            } catch (IllegalArgumentException e) {
                // Atrapamos el error silenciosamente si llega una plataforma que no existe
                System.out.println("Alerta Callahan: Plataforma desconocida ignorada -> " + nombrePlataforma);
            }
        }

        // Unimos la lista con la barra vertical que exige TMDb
        return String.join("|", idsTraducidos);
    }
}


