package com.pps1.guiame.guiame.dao;

import com.pps1.guiame.guiame.Utils;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by javi on 27/04/15.
 */
public class UsuarioDAO {
    private static final String PHP_NAME_NOMBREUSUARIO= "nombreUsuario.php";


    public void getInstance(){

    }

    public String getNombreUsuario(String dni){
        String nombreVariable = "dni";
        Map<String,String> datos = new HashMap<String,String>();
        datos.put(nombreVariable, dni);
        String resultado = Utils.enviarPost(datos, PHP_NAME_NOMBREUSUARIO);
        return  this.usuario(resultado, nombreVariable);
    }




    private String usuario(String response, String nombreVariable)
    {
        String usuario="";
        try
        {
            JSONArray json= new JSONArray(response);
            usuario = json.getJSONObject(0).getString(nombreVariable);
        }
        catch (Exception e)
        {
            // TODO: handle exception
            return "";
        }
        return usuario;
    }
}
