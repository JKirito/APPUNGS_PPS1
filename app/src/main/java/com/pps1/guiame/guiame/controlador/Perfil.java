package com.pps1.guiame.guiame.controlador;

import android.util.Log;

import com.pps1.guiame.guiame.dto.Curso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Perfil
{
    private static String usuario;
    private static String nombre;
    private static String password;
    private static String mail;
    private static int admin;
    private static int id;
    private static List<Curso> cursosUsuario;


    public static String getUsuario()
    {
        return usuario;
    }

    public static void setUsuario(String usuario)
    {
        Perfil.usuario = usuario;
    }

    public static String getPassword()
    {
        return password;
    }

    public static void setPassword(String pass)
    {
        password = pass;
    }

    public static String getNombre() {
        return nombre;
    }

    public static void setNombre(String nombre) {
        Perfil.nombre = nombre;
    }

    public static void setAdmin(int admin){
        Perfil.admin = admin;}

    public static int getId() {
        return id;
    }

    public static void setId(int id) {
        Perfil.id = id;
    }

    public static String getMail(){return mail;}

    public static void setMail(String mail)
    {
        Perfil.mail = mail;
    }

    public static List<Curso> getCursosUsuario()
    {
        return cursosUsuario;
    }

    public static void setCursosUsuario(List<Curso> cursosUsuario)
    {
        Perfil.cursosUsuario = cursosUsuario;
    }

    public static void agregarCurso(Curso curso) {
        if(cursosUsuario == null)
            cursosUsuario = new ArrayList<Curso>();

        String[] diasHorariosAulas = curso.getDiasYHorarios().split(",");
        final String txtAula = "Aula:";
        final String txtDia = "Dias:";
        Map<String, String> aulasDias = new HashMap<String, String>();
        for(String diaHorario : diasHorariosAulas)
        {
            int aulaIndex = diaHorario.indexOf(txtAula);
            String aula = diaHorario.substring(aulaIndex + txtAula.length()).trim();
            Log.d("aula", aula);
            String diaHora = diaHorario.replace(txtDia, "").replace(txtAula,"").substring(0, aulaIndex);
            Log.d("diaHora", diaHora);
            if(aulasDias.containsKey(aula))
                aulasDias.put(aula, aulasDias.get(aula)+", "+diaHora);
            else
                aulasDias.put(aula, diaHora);
        }
        Log.d("aaaaaaaaaaaaaa", "--------");
        for(String aula : aulasDias.keySet())
        {
            Log.d("AU", aula);
            Curso C = new Curso(curso.getId(), curso.getNombre(), curso.getComision(), aula, curso.getDocente(), aulasDias.get(aula));
            cursosUsuario.add(C);
        }
    }

    public static void eliminarCurso(Curso curso)
    {
        ArrayList<Curso> cursosEliminar = new ArrayList<Curso>();
        Log.d("antes", getCursosUsuario().size() + "");
        for(Curso C : getCursosUsuario())
        {
            if(C.getId().equals(curso.getId()))
                cursosEliminar.add(C);
        }

        getCursosUsuario().removeAll(cursosEliminar);
        if( getCursosUsuario().isEmpty())
            cursosUsuario = null;
        Log.d("despues", cursosUsuario != null ? getCursosUsuario().size() + "" : "null!");
    }


    //Cerramos sesión del usuario conectado
    public static void logout()
    {
        usuario = null;
        password = null;
        nombre = null;
        admin = 0;
        mail = null;
        cursosUsuario = null;
    }

    //Verificamos si el usuario ha iniciado sesion
    public static boolean isUserOn()
    {
        if(usuario != null && password != null)
        {
            return true;
        }
        return false;
    }

    //Verificamos si el usuario conectado es administrador o no
    public static Boolean isAdministrador()
    {
        if(admin == 1)
            return true;
        return false;
    }
}
