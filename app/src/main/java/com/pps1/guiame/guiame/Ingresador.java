package com.pps1.guiame.guiame;

import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Ingresador extends ActionBarActivity
{
    private String dni;
    private String pass;
    private Boolean estadoUsuario;

    private final String MSJ_DNI_INVALIDO = "DNI invalido";
    private final String MSJ_PASS_NULL = "Debe ingresar la contraseña";

    private final String PHP_NAME_INGRESADOR= "login.php";


    //@Override
    //protected void onCreate(Bundle savedInstanceState)
    //{
      //  super.onCreate(savedInstanceState);
    //}

    public Ingresador(String dni, String pass)
    {
        this.dni = dni;
        this.pass = pass;
    }

    public List<String> validarDatos()
    {
        List<String> errores = new ArrayList<String>();
        if(dni == null || dni.length() != 8)
        {
            errores.add(MSJ_DNI_INVALIDO);
        }

        if(this.pass == null)
        {
            errores.add(MSJ_PASS_NULL);
        }
        return errores;
    }

    public List<String> ingresarUsuario()
    {
        List<String> errores = this.validarDatos();
        Boolean isUsuarioValido = this.validarUsuario();
        if(!isUsuarioValido)
            errores.add("Usuario inválido. Revise sus datos");

        if(errores.size() > 0)
        {
            return errores;
        }

        return errores;
    }

    public Boolean isUsuarioValido(String response)
    {
        Boolean isValido = false;
        try
        {
            JSONArray json= new JSONArray(response);
            String cantidadRegistrados="";
            cantidadRegistrados = json.getJSONObject(0).getString("COUNT(*)");

            if( cantidadRegistrados.toString().equals("1"))//Si hay un registrado que tiene ese dni
            {                                               //y contraseña, entonces es valido
                isValido = true;
            }
            else
            {
                isValido = false;
            }
        }
        catch (Exception e)
        {
            // TODO: handle exception
        }
        return isValido;
    }

    public Boolean validarUsuario()
    {
        estadoUsuario = false; //Lo inicializo por las dudas

                Map<String,String> datos = new HashMap<String,String>();
                datos.put("dni", dni);
                datos.put("contrasena",pass);
                final String resultado = Utils.enviarPost(datos,PHP_NAME_INGRESADOR);
                Log.d("Resultado count", resultado);
                estadoUsuario = isUsuarioValido(resultado);
        return estadoUsuario;
    }
}
