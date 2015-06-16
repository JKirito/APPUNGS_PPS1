package com.pps1.guiame.guiame.persistencia.dao;

import android.util.Log;

import com.pps1.guiame.guiame.dto.Aula;
import com.pps1.guiame.guiame.dto.Curso;
import com.pps1.guiame.guiame.persistencia.conexion.Conexion;

import org.json.JSONArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Listador
{
    private Integer idUsuario;
    private String textoParaFiltrar;
    private final String PHP_NAME_LISTADOR_JUNTO = "listarCursosJuntos.php";
    private final String PHP_NAME_LISTADOR_CURSO = "listarCursos.php";

    public Listador(Integer idUsuario)
    {
        this.idUsuario = idUsuario;
    }

    public Listador(String textoAFiltrar)
    {
        this.textoParaFiltrar = textoAFiltrar;
    }

    public Listador(Integer idUsuario,String textoParaFiltrar)
    {
        this.idUsuario = idUsuario;
        this.textoParaFiltrar = textoParaFiltrar;
    }




    //Devolvemos una lista con los cursos que coinciden con el nombre de la materia que ingreso el usuario
    public ArrayList<Curso> getListadoCursosJuntos() throws Exception
    {
        Map<String, String> datos = new HashMap<String, String>();
        datos.put("texto", textoParaFiltrar);
        String result = Conexion.enviarPost(datos, PHP_NAME_LISTADOR_JUNTO);

        return obtDatosJSONCursosJuntos(result);
    }

    //Devolvemos una lista con los cursos que coinciden con el nombre de la materia que ingreso el usuario
    //Cada item tendra un aula
    public ArrayList<Curso> getListadoCursos() throws Exception {
        Map<String, String> datos = new HashMap<String, String>();
        datos.put("texto", textoParaFiltrar);
        String result = Conexion.enviarPost(datos, PHP_NAME_LISTADOR_CURSO);

        return obtDatosJSONCursos(result);
    }

    public ArrayList<Curso> obtDatosJSONCursosJuntos(String response)
    {
        ArrayList<Curso> listado= new ArrayList<Curso>();
        try
        {
            JSONArray json= new JSONArray(response);
            Curso c;
            for (int i=0; i<json.length();i++)
            {
                Integer id = json.getJSONObject(i).getInt("curso");
                String nombreCurso = json.getJSONObject(i).getString("alias");
                String comision = json.getJSONObject(i).getString("comision");
                String horarioAula = json.getJSONObject(i).getString("horario");
                String prof = json.getJSONObject(i).getString("nombre");
                c = new Curso(id, nombreCurso, comision, prof, horarioAula);
                listado.add(c);
            }
        }
        catch (Exception e)
        {
            Log.d("EXCEP obtDatoJSONCurso", e+"");
        }
        return listado;
    }

    public ArrayList<Curso> obtDatosJSONCursos(String response)
    {
        ArrayList<Curso> listado= new ArrayList<Curso>();
        try
        {
            JSONArray json= new JSONArray(response);
            Curso c;
            for (int i=0; i<json.length();i++)
            {
                Integer id = json.getJSONObject(i).getInt("curso");
                String nombreCurso = json.getJSONObject(i).getString("alias");
                String comision = json.getJSONObject(i).getString("comision");
                String aula = json.getJSONObject(i).getString("numero");
                String horario = json.getJSONObject(i).getString("horario");
                String prof = json.getJSONObject(i).getString("nombre");
                c = new Curso(id, nombreCurso, comision, aula, prof, horario);
                listado.add(c);
            }
        }
        catch (Exception e)
        {
            Log.d("EXCEP obtDatoJSONCurso", e+"");
        }
        return listado;
    }

}