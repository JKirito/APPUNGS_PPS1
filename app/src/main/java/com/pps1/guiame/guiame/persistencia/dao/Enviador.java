package com.pps1.guiame.guiame.persistencia.dao;

import android.util.Log;
import com.pps1.guiame.guiame.persistencia.conexion.Conexion;
import java.util.HashMap;
import java.util.Map;

public class Enviador
{
    private String mail;
    private String nombreYApellido;
    private final String PHP_NAME_ENVIADOR_SALUDO= "/mail/saludo.php";
    private final String PHP_NAME_ENVIADOR_RECUPERACION= "/mail/recuperarContra.php";

    public Enviador(String nombreYApellido,String mail)
    {
        this.mail= mail;
        this.nombreYApellido = nombreYApellido;
    }

    public Enviador(String mail)
    {
        this.mail= mail;
    }

    public void enviarMailBienvenida() throws Exception
    {
        Map<String, String> datos = new HashMap<String, String>();
        datos.put("nombreYApellido",nombreYApellido);
        datos.put("mail",mail);
        String result = Conexion.enviarPost(datos, PHP_NAME_ENVIADOR_SALUDO);
        Log.d("result mailBienvenida", result);
    }

    public void enviarMailRecuperarContraseña() throws Exception
    {
        Map<String, String> datos = new HashMap<String, String>();
        datos.put("mail",mail);

        String result = Conexion.enviarPost(datos, PHP_NAME_ENVIADOR_RECUPERACION);
        Log.d("result mailRecuperacion", result);

    }

}
