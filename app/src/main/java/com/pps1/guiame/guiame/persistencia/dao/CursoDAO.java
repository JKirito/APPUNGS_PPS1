package com.pps1.guiame.guiame.persistencia.dao;

import android.util.Log;

import com.pps1.guiame.guiame.controlador.Perfil;
import com.pps1.guiame.guiame.dto.Curso;
import com.pps1.guiame.guiame.persistencia.conexion.Conexion;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CursoDAO
{
    private final String PHP_NAME_CURSO_PERSONAL = "registrarCursoPersonalizado.php";
    private final String PHP_NAME_BORRADOR_CURSO = "borrarCurso.php";
    private final String PHP_NAME_LISTADOR = "listarMateriasUsuario.php";

    public void registrarCursoPersonalizado(Curso curso, Integer idUsuario) throws Exception {
        Map<String, String> datos = new HashMap<String, String>();
        datos.put("idUsuario",idUsuario.toString());
        datos.put("idCurso", curso.getId().toString());
        datos.put("nombre", curso.getNombre().toString());

        String result = Conexion.enviarPost(datos, PHP_NAME_CURSO_PERSONAL);

        //Si se agrego en la base, lo agrego a la caché
        Perfil.agregarCurso(curso);
    }

    public void eliminarCurso(Curso curso, Integer idUsuario) throws Exception {
        //La key del map deben ser los nombres de los campos en la tabla
        Map<String, String> datos = new HashMap<String, String>();
        datos.put("id_cursos",curso.getId().toString());
        datos.put("id_usuarios", idUsuario.toString());
        //datos.put("nomPersonalizado",nombreMateria);

        String result = Conexion.enviarPost(datos, PHP_NAME_BORRADOR_CURSO);

        //Si se eliminó en la base, lo elimino de la caché
        Perfil.eliminarCurso(curso);
    }

    public ArrayList<Curso> getListadoCursosUsuario(Integer idUsuario) throws Exception {
        //Si tengo los cursos guardados, uso estos
        if(Perfil.getCursosUsuario() != null)
            return (ArrayList<Curso>) Perfil.getCursosUsuario();

        //Si no los tengo, entonces por primera vez los busco en el servidor y los guardo

        //La key del map deben ser los nombres de los campos en la tabla
        Map<String, String> datos = new HashMap<String, String>();
        datos.put("id",idUsuario.toString());

        String result = Conexion.enviarPost(datos, PHP_NAME_LISTADOR);
        ArrayList<Curso> listadoCursos = obtDatosJSONMateriasUsuario(result);

        Log.d("resultPostListadoMatUS", result);
        // Guardo los cursos en la caché para las próximas consultas
        Perfil.setCursosUsuario(listadoCursos);

        return listadoCursos;
    }

    public ArrayList<Curso> obtDatosJSONMateriasUsuario(String response)
    {
        ArrayList<Curso> listado= new ArrayList<Curso>();
        if(response == null || response.equals("null"))
            return listado;
        Log.d("response En obtDatos",response);
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
}
