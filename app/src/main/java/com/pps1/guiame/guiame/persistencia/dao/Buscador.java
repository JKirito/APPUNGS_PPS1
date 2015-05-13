package com.pps1.guiame.guiame.persistencia.dao;

import android.util.Log;

import com.pps1.guiame.guiame.persistencia.conexion.Conexion;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Buscador
{
    private String dni;
    private final String PHP_NAME_BUSCADOR_ID = "obtenerIdUsuario.php";
    private final String PHP_NAME_CURSO_PERSONAL = "registrarCursoPersonalizado.php";

    public Buscador(String dni)
    {
        this.dni = dni;
    }

    private String getIdUsuario()
    {
        //La key del map deben ser los nombres de los campos en la tabla
        Map<String, String> datos = new HashMap<String, String>();
        datos.put("dni",dni);

        String result = Conexion.enviarPost(datos, PHP_NAME_BUSCADOR_ID);
        String idUsuario = obtIdJSON(result);

        return idUsuario;
    }

    public void registrarCursoPersonalizado(String idCurso)
    {
        String idUsuario = getIdUsuario();
        Map<String, String> datos = new HashMap<String, String>();
        datos.put("idUsuario",idUsuario);
        datos.put("idCurso",idCurso);

        String result = Conexion.enviarPost(datos, PHP_NAME_CURSO_PERSONAL);
        Log.d("RESULTADOU", result);
    }

    private String obtIdJSON(String response)
    {
        String id = "";
        try
        {
            JSONArray json= new JSONArray(response);
            id = json.getJSONObject(0).getString("id");
        }
        catch (Exception e)
        {
            Log.d("EXCEPCION obtDatosJSON", e+"");
        }
        return id;
    }

}
