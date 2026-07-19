package com.callahan.callahancodigofuente.models;

public enum EpocasPeliculas {


    CINE_CLASICO(1890, 1969),
    LOS_SETENTAS(1970,1979),
    LOS_OCHENTAS(1980,1989),
    LOS_NOVENTAS(1990,1999),
    LOS_DOSMIL(2000,2009),
    DECADA_2010(2010,2019),
    ACTUALIDAD(2020,2030);

    private final int anioInicio;
    private final int anioFin;

    EpocasPeliculas(int anioInicio, int anioFin) {
        this.anioInicio = anioInicio;
        this.anioFin = anioFin;
    }

    public static EpocasPeliculas obtenerEpoca(int anioEstreno) {

        //values() nos da todos los elementos del enum
        for (EpocasPeliculas epoca : values()){

            if (anioEstreno >= epoca.anioInicio &&  anioEstreno <= epoca.anioFin ){

                return  epoca;


            }


        }

        return  CINE_CLASICO;
    }

}
