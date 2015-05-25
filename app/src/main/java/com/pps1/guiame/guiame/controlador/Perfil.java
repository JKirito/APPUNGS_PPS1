package com.pps1.guiame.guiame.controlador;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.pps1.guiame.guiame.R;

public class Perfil
{
    private static String usuario;
    private static String nombre;
    private static String password;
    private static String mail;
    private static int admin;
    private static int id;


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

    public static int getAdmin()
    {
        return admin;
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

    public static void setMail(String mail) {
        Perfil.mail = mail; }

    //Cerramos sesión del usuario conectado
    public static void logout()
    {
        usuario = null;
        password = null;
        nombre = null;
        admin = 0;
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
