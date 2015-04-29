package com.pps1.guiame.guiame.persistencia.dao;

import android.util.Log;

import com.pps1.guiame.guiame.persistencia.conexion.Utils;
import com.pps1.guiame.guiame.dto.Aula;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

public class BuscadorAula
{

    private Aula aula;
    private String numAula;
    private double latitud;
    private double longitud;
    private final String SIN_AULA = "SIN AULA";
    private final String PHPNAME_OBTENERCOORDENADA = "obtenerCoordenada.php";


    /**
     * Obtiene el número del aula del parámetro recibido y, si existe, obtiene las coordenadas
     * geográficas de esa aula
     * @param itemSeleccionado
     */
    public BuscadorAula(String itemSeleccionado)
    {
        this.numAula = obtenerAula(itemSeleccionado);
        String[] coordenada = obtenerCoordenada();
        if(coordenada.length == 2)
        {
            this.latitud = Double.parseDouble(coordenada[0]);
            this.longitud = Double.parseDouble(coordenada[1]);
        }
    }


    public Aula getAula(){
        if(numAula == null){
            return null;
        }
        return new Aula(numAula, latitud, longitud);
    }

    /**
     * Devuelve el número del aula dentro del texto que recibe como parámetro.
     *
     * @param itemSeleccionado debe ser algo como "PPS1 - Aula:7070 - COM - DIA HORARIO - PROF"
     */
    private String obtenerAula(String itemSeleccionado)
    {
        if(itemSeleccionado != null && !itemSeleccionado.isEmpty()) {
            int posInicio = itemSeleccionado.indexOf(":");
            int posFin = itemSeleccionado.indexOf("-", posInicio);
            String aula;
            try
            {
                aula = itemSeleccionado.substring(posInicio + 1, posFin).trim();
            } catch (Exception e)
            {
                return SIN_AULA;
            }
            return aula;
        }
        return SIN_AULA;
    }

    /**
     *
     * @return array con latitud en primera posicion y longitud en segunda posicion o array vacío
     * si no encuentra nada
     */
    private String[] obtenerCoordenada()
    {
        Map<String, String> data = new HashMap<String, String>();
        data.put("aula", this.numAula);

        String response = Utils.enviarPost(data, PHPNAME_OBTENERCOORDENADA);

        String result = obtDatosJSON(response);
        if(result != null && !result.isEmpty())
        {
            //Se espera que llega "latitud;longitud"
            String[] coordenadas = result.split(",");
            if( coordenadas != null && coordenadas.length == 2)
            {
                return coordenadas;
            }
        }
        return new String[0];
    }

    private String obtDatosJSON(String response)
    {
        String coordenadas = "";
        try {
            JSONArray json= new JSONArray(response);
            coordenadas = json.getJSONObject(0).getString("ubicacion");
        }
        catch (Exception e)
        {
            Log.d("EXCEP obtDatJSONBusAula", e+"");
        }
        return coordenadas;
    }

    public String getNumAula()
    {
        return this.numAula;
    }
}
