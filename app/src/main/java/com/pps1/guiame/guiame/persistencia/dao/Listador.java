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
    private String numeroAula;
    private final String PHP_NAME_LISTADOR = "listarMateriasUsuario.php";
    private final String PHP_NAME_LISTADOR_JUNTO = "listarCursosJuntos.php";
    private final String PHP_NAME_LISTADOR_CURSO = "listarCursos.php";
    private final String PHP_NAME_LISTADOR_AULAS = "listarAulas.php";

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


    public ArrayList<Curso> getListadoCursosUsuario() throws IOException {
        //La key del map deben ser los nombres de los campos en la tabla
        Map<String, String> datos = new HashMap<String, String>();
        datos.put("id",idUsuario.toString());

        String result = Conexion.enviarPost(datos, PHP_NAME_LISTADOR);
        ArrayList<Curso> listadoCursos = obtDatosJSONMateriasUsuario(result);

        Log.d("resultPostListadoMatUS", result);

        return listadoCursos;
    }

    public ArrayList<Aula> getListadoAulas() throws IOException
    {
        Map<String, String> datos = new HashMap<String, String>();
        datos.put("texto", textoParaFiltrar);
        String result = Conexion.enviarPost(datos, PHP_NAME_LISTADOR_AULAS);

        return obtDatosJSONAulas(result);
    }

    //Devolvemos una lista con los cursos que coinciden con el nombre de la materia que ingreso el usuario
    public ArrayList<Curso> getListadoCursosJuntos() throws IOException {
        Map<String, String> datos = new HashMap<String, String>();
        datos.put("texto", textoParaFiltrar);
        String result = Conexion.enviarPost(datos, PHP_NAME_LISTADOR_JUNTO);

        return obtDatosJSONCursosJuntos(result);
    }

    //Devolvemos una lista con los cursos que coinciden con el nombre de la materia que ingreso el usuario
    //Cada item tendra un aula
    public ArrayList<Curso> getListadoCursos() throws IOException {
        Map<String, String> datos = new HashMap<String, String>();
        datos.put("texto", textoParaFiltrar);
        String result = Conexion.enviarPost(datos, PHP_NAME_LISTADOR_CURSO);

        return obtDatosJSONCursos(result);
    }

    public ArrayList<Curso> obtDatosJSONMateriasUsuario(String response)
    {
        ArrayList<Curso> listado= new ArrayList<Curso>();
        try
        {
            JSONArray json= new JSONArray(response);
            Curso curso;
            for (int i=0; i<json.length();i++)
            {
                int idCurso = json.getJSONObject(i).getInt("curso");
                String nombreCurso = json.getJSONObject(i).getString("alias");
                String aula = json.getJSONObject(i).getString("numero");
                String comision = json.getJSONObject(i).getString("comision");
                String horario = json.getJSONObject(i).getString("horario");
                String prof = json.getJSONObject(i).getString("nombre");
                curso = new Curso(idCurso, nombreCurso, comision, aula, prof, horario);
                listado.add(curso);
            }
        }
        catch (Exception e)
        {
            Log.d("EXCEP obtDatosJSONMatUs", e+"");
        }
        return listado;
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

    public ArrayList<Aula> obtDatosJSONAulas(String response)
    {
        ArrayList<Aula> listado= new ArrayList<Aula>();
        try
        {
            JSONArray json= new JSONArray(response);
            Aula a;
            for (int i=0; i<json.length();i++)
            {
                String aula = json.getJSONObject(i).getString("numero");
                a = new Aula(aula,0.0,0.0);
                listado.add(a);
            }
        }
        catch (Exception e)
        {
            Log.d("EXCEP obtDatosJSONAulas", e+"");
        }
        return listado;
    }

}