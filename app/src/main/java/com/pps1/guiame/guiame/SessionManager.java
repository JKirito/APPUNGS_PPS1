package com.pps1.guiame.guiame;

import java.util.Date;

public class SessionManager
{

    private static String user;
    private static String password;
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
    }

    public static boolean isUserOn()
    {
        if(user != null && password != null)
        {
            return true;
        }
        return false;
    }

}