package com.pps1.guiame.guiame.dto;

import java.io.Serializable;

/**
 * Created by javi on 11/04/15.
 */
public class Aula implements Serializable
{
    private String numAula;
    private Double latitud;
    private Double longitud;

    public Aula(String numAula, Double latitud, Double longitud)
    {
        this.numAula = numAula;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public String getNumAula()
    {
        return numAula;
    }

    public Double getLatitud()
    {
        return latitud;
    }

    public Double getLongitud()
    {
        return longitud;
    }

}
