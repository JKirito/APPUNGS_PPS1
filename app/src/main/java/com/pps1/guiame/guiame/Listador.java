package com.pps1.guiame.guiame;

import android.util.Log;
import android.widget.ArrayAdapter;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Agustina on 30/03/2015.
 */
public class Listador
{
    private String dni;
    private final String PHP_NAME_LISTADOR = "listarMateriasUsuario.php";
    private final String PHP_NAME_LISTADOR_TODO = "listarMaterias.php";

    public Listador(){}

    public Listador(String dni)
    {
        this.dni = dni;
    }


    public ArrayList<String> getListadoMateriasUsuario()
    {
        //La key del map deben ser los nombres de los campos en la tabla
        Map<String, String> datos = new HashMap<String, String>();
        datos.put("dni",dni);

        String result = Utils.enviarPost(datos, PHP_NAME_LISTADOR);
        ArrayList<String> listadoMaterias = obtDatosJSON(result);
        //TODO: qué hago con el result?
        Log.d("result post", result);

        return listadoMaterias;
    }

    public ArrayList<String> getListadoMaterias()
    {
        String result = Utils.getPHPResult(PHP_NAME_LISTADOR_TODO); //Obtenemos el resultado del query

        ArrayList<String> listadoMaterias = obtDatosJSON(result); //Resultado con formato para lista

        //TODO: qué hago con el result?
        Log.d("result post", result);

        return listadoMaterias; //Devolvemos lista de materias
    }

    public ArrayList<String> obtDatosJSON(String response)
    {
        ArrayList<String> listado= new ArrayList<String>();
        try {
            JSONArray json= new JSONArray(response);
            String texto;
            for (int i=0; i<json.length();i++)
            {
                texto = json.getJSONObject(i).getString("alias") +"- Aula: "+
                        json.getJSONObject(i).getString("numero") +" - "+
                        json.getJSONObject(i).getString("comision") +" - "+
                        json.getJSONObject(i).getString("dia") +" de "+
                        json.getJSONObject(i).getString("horaInicio") +" a "+
                        json.getJSONObject(i).getString("horaFin") +" - "+
                        json.getJSONObject(i).getString("nombre");
                Log.d("texto",texto);
                listado.add(texto);
            }
        }
        catch (Exception e)
        {
            Log.d("EXCEPCION obtDatosJSON", e+"");
        }
        return listado;
    }


}