package com.pps1.guiame.guiame.persistencia.dao;

import android.util.Log;

import com.pps1.guiame.guiame.dto.Aula;
import com.pps1.guiame.guiame.persistencia.conexion.Conexion;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AulaDAO
{
    private final String PHP_NAME_GEOLOCALIZADOR_AULA = "geolocalizarAula.php";
    private final String PHPNAME_OBTENERCOORDENADA = "obtenerCoordenada.php";
    private final String PHP_NAME_LISTADOR_AULAS = "listarAulas.php";

    public void geolocalizarAula(Aula aula) throws Exception
    {
        String ubicacion = aula.getLatitud()+","+aula.getLongitud();
        //La key del map deben ser los nombres de los campos en la tabla
        Map<String, String> datos = new HashMap<String, String>();
        datos.put("numero",aula.getNumAula());
        datos.put("ubicacion", ubicacion);

        Conexion.enviarPost(datos, PHP_NAME_GEOLOCALIZADOR_AULA);
    }

    public ArrayList<Aula> getListadoAulas(String textoParaFiltrar) throws Exception
    {
        Map<String, String> datos = new HashMap<String, String>();
        datos.put("texto", textoParaFiltrar);
        String result = Conexion.enviarPost(datos, PHP_NAME_LISTADOR_AULAS);

        return getListadoAulasFromJSON(result);
    }

    public Aula getAula(String numAula) throws Exception {
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
        Double latitud = ubicacion != null && !ubicacion.isEmpty() ? Double.valueOf(ubicacion.split(",")[0]) : null;
        Double longitud = ubicacion != null && !ubicacion.isEmpty() ? Double.valueOf(ubicacion.split(",")[1]) : null;
        return new Aula(numAula, latitud, longitud);
    }

    public ArrayList<Aula> getListadoAulasFromJSON(String response)
    {
        ArrayList<Aula> listado= new ArrayList<Aula>();
        try
        {
            JSONArray json= new JSONArray(response);
            Aula a;
            for (int i=0; i<json.length();i++)
            {
                String aula = json.getJSONObject(i).getString("numero");
                String ubicacion = json.getJSONObject(i).getString("ubicacion");
                Double latitud = ubicacion != null && !ubicacion.isEmpty() ? Double.valueOf(ubicacion.split(",")[0]) : null;
                Double longitud = ubicacion != null && !ubicacion.isEmpty() ? Double.valueOf(ubicacion.split(",")[1]) : null;
                a = new Aula(aula,latitud,longitud);
                listado.add(a);
            }
        }
        catch (Exception e)
        {
            Log.d("EXCEP getListAulasJSON", e+"");
        }
        return listado;
    }
}
