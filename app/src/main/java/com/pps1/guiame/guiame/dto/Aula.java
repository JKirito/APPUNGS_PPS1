package com.pps1.guiame.guiame.dto;

import java.io.Serializable;

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

    public void setLatitud(Double latitud) { this.latitud = latitud;}

    public void setLongitud(Double longitud) { this.longitud = longitud;}

    @Override
    public String toString() {
        return numAula;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Aula aula = (Aula) o;

        if (!numAula.equals(aula.numAula)) return false;
        if (!latitud.equals(aula.latitud)) return false;
        return longitud.equals(aula.longitud);

    }

    @Override
    public int hashCode() {
        int result = numAula.hashCode();
        result = 31 * result + latitud.hashCode();
        result = 31 * result + longitud.hashCode();
        return result;
    }
}
