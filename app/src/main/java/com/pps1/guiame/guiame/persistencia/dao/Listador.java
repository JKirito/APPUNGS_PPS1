package com.pps1.guiame.guiame.persistencia.dao;

import android.util.Log;

import com.pps1.guiame.guiame.dto.Curso;
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
  //  private final String PHP_NAME_LISTADOR_TODO = "listarMaterias.php"; BORRAR
    private final String PHP_NAME_LISTADOR_JUNTO = "listarCursosJuntos.php";

    public Listador(Integer idUsuario)
    {
        this.idUsuario = idUsuario;
    }

    public Listador(Integer idUsuario,String textoCurso)
    {
        this.idUsuario = idUsuario;
        this.textoCurso = textoCurso;
    }


    public ArrayList<Curso> getListadoCursosUsuario()
    {
        //La key del map deben ser los nombres de los campos en la tabla
        Map<String, String> datos = new HashMap<String, String>();
        datos.put("id",idUsuario.toString());

        String result = Conexion.enviarPost(datos, PHP_NAME_LISTADOR);
        ArrayList<Curso> listadoCursos = obtDatosJSON(result);

        Log.d("resultPostListadoMatUS", result);

        return listadoCursos;
    }

    //BORRAR!
   /* public ArrayList<String> getListadoCursosDisponibles()
    {
        String result = Conexion.getPHPResult(PHP_NAME_LISTADOR_TODO); //Obtenemos el resultado del query

        ArrayList<String> listadoCursosDisponibles = obtDatosJSON(result); //Resultado con formato para lista

        //TODO: qué hago con el result?
        Log.d("resultPostListadoMat", result == null ? "null :(": result);

        return listadoCursosDisponibles; //Devolvemos lista de cursos
    }*/

    //Devolvemos una lista con los cursos que coinciden con el nombre de la materia que ingreso el usuario
    public ArrayList<Curso> getListadoCursosJuntos()
    {
        Map<String, String> datos = new HashMap<String, String>();
        datos.put("texto",textoCurso);
        String result = Conexion.enviarPost(datos, PHP_NAME_LISTADOR_JUNTO);

        return obtDatosJSONCursosJuntos(result);
    }

    public ArrayList<Curso> obtDatosJSON(String response)
    {
        ArrayList<Curso> listado= new ArrayList<Curso>();
        try
        {
            JSONArray json= new JSONArray(response);
            Curso curso;
            for (int i=0; i<json.length();i++)
            {
                int idCurso = json.getJSONObject(i).getInt("id_cursos");
                String nombreCurso = json.getJSONObject(i).getString("nomPersonalizado");
                String aula = json.getJSONObject(i).getString("numero");
                String comision = json.getJSONObject(i).getString("comision")+" Dia: ";
                String dia = json.getJSONObject(i).getString("dia")+" de ";
                String horaInicio = json.getJSONObject(i).getString("horaInicio")+" a ";
                String horaFin = json.getJSONObject(i).getString("horaFin")+" - ";
                String prof = json.getJSONObject(i).getString("nombre");
                curso = new Curso(idCurso, nombreCurso, comision, aula, prof, dia+horaInicio+horaFin);
                listado.add(curso);
            }
        }
        catch (Exception e)
        {
            Log.d("EXCEPCION obtDatosJSON", e+"");
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
                String aula = json.getJSONObject(i).getString("numero");
                String comision = json.getJSONObject(i).getString("comision");
                String horario = json.getJSONObject(i).getString("horario");
                String prof = json.getJSONObject(i).getString("nombre");
                c = new Curso(id, nombreCurso, comision, aula, prof, horario);
                listado.add(c);
            }
        }
        catch (Exception e)
        {
            Log.d("EXCEPCION obtDatosJSON", e+"");
        }
        return listado;
    }


}