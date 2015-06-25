package com.pps1.guiame.guiame.persistencia.dao;

import android.util.Log;

import com.pps1.guiame.guiame.persistencia.conexion.Conexion;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

public class Enviador
{
    private String mail;
    private String nombreYApellido;
    private final String PHP_NAME_ENVIADOR_SALUDO= "/mail/saludo.php";
    private final String PHP_NAME_ENVIADOR_RECUPERACION= "/mail/recuperarContra.php";
    private final String PHP_NAME_VERIFICAR_MAIL= "/mail/verificarMail.php";

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

    public boolean isMailExistente() throws Exception {
        Map<String, String> datos = new HashMap<String, String>();
        datos.put("mail", mail);

        String result = Conexion.enviarPost(datos, PHP_NAME_VERIFICAR_MAIL);

        String mailExiste = "";
        try {
            JSONArray json = new JSONArray(result);
            mailExiste = json.getJSONObject(0).getString("COUNT(*)");
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Sólo se permite un único mail!
        return mailExiste.toString().equals("1");
    }
}
