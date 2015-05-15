package com.pps1.guiame.guiame.persistencia.dao;

import android.util.Log;

import com.pps1.guiame.guiame.persistencia.conexion.Conexion;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Listador
{
    private Integer idUsuario;
    private String textoCurso;
    private final String PHP_NAME_LISTADOR = "listarMateriasUsuario.php";
    private final String PHP_NAME_LISTADOR_TODO = "listarMaterias.php";
    private final String PHP_NAME_LISTADOR_JUNTO = "listarCursosJuntos.php";

    public Listador(){}

    public Listador(Integer idUsuario)
    {
        this.idUsuario = idUsuario;
    }

    public Listador(Integer idUsuario,String textoCurso)
    {
        this.idUsuario = idUsuario;
        this.textoCurso = textoCurso;
    }


    public ArrayList<String> getListadoCursosUsuario()
    {
        //La key del map deben ser los nombres de los campos en la tabla
        Map<String, String> datos = new HashMap<String, String>();
        datos.put("id",idUsuario.toString());

        String result = Conexion.enviarPost(datos, PHP_NAME_LISTADOR);
        ArrayList<String> listadoCursos = obtDatosJSON(result);

        Log.d("resultPostListadoMatUS", result);

        return listadoCursos;
    }

    public ArrayList<String> getListadoCursosDisponibles()
    {
        String result = Conexion.getPHPResult(PHP_NAME_LISTADOR_TODO); //Obtenemos el resultado del query

        ArrayList<String> listadoCursosDisponibles = obtDatosJSON(result); //Resultado con formato para lista

        //TODO: qué hago con el result?
        Log.d("resultPostListadoMat", result == null ? "null :(": result);

        return listadoCursosDisponibles; //Devolvemos lista de cursos
    }

    //Devolvemos una lista con los cursos que coinciden con el nombre de la materia que ingreso el usuario
    public ArrayList<String> getListadoCursosJuntos()
    {
        Map<String, String> datos = new HashMap<String, String>();
        datos.put("texto",textoCurso);
        String result = Conexion.enviarPost(datos, PHP_NAME_LISTADOR_JUNTO);

        ArrayList<String> listadoCursos = obtDatosJSONCursosJuntos(result);

        //TODO: qué hago con el result?
        Log.d("resultPostListadoCurso", result == null ? "null :(": result);

        return listadoCursos;
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

    public ArrayList<String> obtDatosJSONCursosJuntos(String response)
    {
        ArrayList<String> listado= new ArrayList<String>();
        try {
            JSONArray json= new JSONArray(response);
            String texto;
            for (int i=0; i<json.length();i++)
            {
                texto = json.getJSONObject(i).getString("curso") +" - "+
                        json.getJSONObject(i).getString("alias") +"- Aula: "+
                        json.getJSONObject(i).getString("numero") +" - "+
                        json.getJSONObject(i).getString("comision") +" - "+
                        json.getJSONObject(i).getString("horario")+" - "+
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