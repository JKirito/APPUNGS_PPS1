package com.pps1.guiame.guiame.persistencia.dao;

import android.util.Log;

import com.pps1.guiame.guiame.dto.Aula;
import com.pps1.guiame.guiame.persistencia.conexion.Conexion;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by javi on 29/04/15.
 */
public class AulaDAO {
    private final String PHP_NAME_REGISTRADOR_AULA = "registrarAula.php";
    private final String PHPNAME_OBTENERCOORDENADA = "obtenerCoordenada.php";

    public void registrarAula(Aula aula)
    {
        String ubicacion = aula.getLatitud()+","+aula.getLongitud();
        //La key del map deben ser los nombres de los campos en la tabla
        Map<String, String> datos = new HashMap<String, String>();
        datos.put("numero",aula.getNumAula());
        datos.put("ubicacion", ubicacion);

        String result = Conexion.enviarPost(datos, PHP_NAME_REGISTRADOR_AULA);
    }

    public Aula getAula(String numAula)
    {
        Map<String, String> data = new HashMap<String, String>();
        data.put("aula", numAula);

        String response = Conexion.enviarPost(data, PHPNAME_OBTENERCOORDENADA);

        return getAulaFromJSON(response, numAula);
    }

    private Aula getAulaFromJSON(String response, String numAula)
    {
        String ubicacion = "";
        try
        {
            JSONArray json= new JSONArray(response);
            ubicacion = json.getJSONObject(0).getString("ubicacion");
        }
        catch (Exception e)
        {
            Log.d("EXCEP obtDatJSONBusAula", e + "");
        }
        Double latitud = ubicacion != null ? Double.valueOf(ubicacion.split(",")[0]) : 0.0;
        Double longitud = ubicacion != null ? Double.valueOf(ubicacion.split(",")[1]) : 0.0;
        return new Aula(numAula, latitud, longitud);
    }
}
