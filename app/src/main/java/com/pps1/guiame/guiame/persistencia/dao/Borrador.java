package com.pps1.guiame.guiame.persistencia.dao;

import android.util.Log;

import com.pps1.guiame.guiame.persistencia.conexion.Conexion;

import java.util.HashMap;
import java.util.Map;

public class Borrador
{
    private String idCurso;
    private String idUsuario;
    private String nombreMateria;
    private final String PHP_NAME_BORRADOR_CURSO = "borrarCurso.php";

    public Borrador(String idCurso, String idUsuario, String nombreMateria)
    {
        this.idCurso = idCurso;
        this.idUsuario = idUsuario;
        this.nombreMateria = nombreMateria;
    }

    public void eliminarCurso()
    {
        //La key del map deben ser los nombres de los campos en la tabla
        Map<String, String> datos = new HashMap<String, String>();
        datos.put("id_cursos",idCurso);
        datos.put("id_usuarios",idUsuario);
        datos.put("nomPersonalizado",nombreMateria);

        String result = Conexion.enviarPost(datos, PHP_NAME_BORRADOR_CURSO);
        Log.d("RESULT BORRAR", result);
    }
}
