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

    public Aula(String numAula, double latitud, double longitud)
    {
        this.numAula = numAula;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public String getNumAula()
    {
        return numAula;
    }

    public double getLatitud()
    {
        return latitud;
    }

    public double getLongitud()
    {
        return longitud;
    }

}
