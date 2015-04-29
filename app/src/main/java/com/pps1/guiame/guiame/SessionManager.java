package com.pps1.guiame.guiame;

import java.util.Date;

public class SessionManager
{

    private static String user;
    private static String nombre;
    private static String password;
    private static int admin;
    private static Date initSession;
    private static Date finSession;

    public static String getUser()
    {
        return user;
    }

    public static void setUser(String usuario)
    {
        user = usuario;
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
        SessionManager.nombre = nombre;
    }

    public static int getAdmin()
    {
        return admin;
    }

    public static void setAdmin(int admin){
        SessionManager.admin = admin;}

    public static Date getInitSession()
    {
        return initSession;
    }

    public static void setInitSession(Date initSession)
    {
        SessionManager.initSession = initSession;
    }

    public static Date getFinSession()
    {
        return finSession;
    }

    public static void setFinSession(Date finSession)
    {
        SessionManager.finSession = finSession;
    }

    public static void logout()
    {
        user = null;
        password = null;
        nombre = null;
        admin = 0;
    }

    public static boolean isUserOn()
    {
        if(user != null && password != null)
        {
            return true;
        }
        return false;
    }

    public static Boolean isAdministrador()
    {
        if(admin == 1)
            return true;
        return false;
    }
}
