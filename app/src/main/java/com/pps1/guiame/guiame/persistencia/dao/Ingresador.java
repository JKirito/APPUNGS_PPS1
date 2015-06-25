package com.pps1.guiame.guiame.persistencia.dao;

import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.pps1.guiame.guiame.controlador.Perfil;
import com.pps1.guiame.guiame.persistencia.conexion.Conexion;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;


public class Ingresador extends ActionBarActivity
{
    private String dni;
    private String pass;

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



    public void guardarDatosUsuario(String datosUsuarioJSON) throws Exception {
        JSONArray json = new JSONArray(datosUsuarioJSON);
        //String cantidadRegistrados= json.getJSONObject(0).getString("COUNT(*)");
        String nombre = json.getJSONObject(0).getString("nombre");
        int admin = json.getJSONObject(0).getInt("admin");
        int id = json.getJSONObject(0).getInt("id");
        String mail = json.getJSONObject(0).getString("mail");
        nombre = nombre != null ? nombre.split(" ")[0] : "";
        // Guardo datos en sessionManager
        Perfil.setUsuario(dni);
        Perfil.setPassword(pass);
        Perfil.setNombre(nombre);
        Perfil.setAdmin(admin);
        Perfil.setId(id);
        Perfil.setMail(mail);
    }

    public String resultadoJSON() throws Exception {
        Map<String,String> datos = new HashMap<String,String>();
        datos.put("dni", dni);
        datos.put("contrasena",pass);
        final String resultado = Conexion.enviarPost(datos, PHP_NAME_INGRESADOR);
        Log.d("Resultado count", resultado);
        return resultado;
    }
}