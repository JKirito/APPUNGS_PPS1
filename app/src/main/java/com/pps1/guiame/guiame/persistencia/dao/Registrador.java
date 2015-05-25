package com.pps1.guiame.guiame.persistencia.dao;

import android.util.Log;

import com.pps1.guiame.guiame.persistencia.conexion.Conexion;

import java.util.HashMap;
import java.util.Map;

public class Registrador
{
    private String nombreYApellido;
    private String dni;
    private String mail;
    private String pass;
    private String pass2;


    private final String PHP_NAME_REGISTRADOR = "registrarUsuario.php";


    public Registrador(String nombreYApellido, String dni, String mail, String pass, String pass2)
    {
        this.nombreYApellido = nombreYApellido;
        this.dni = dni;
        this.mail = mail;
        this.pass = pass;
        this.pass2 = pass2;
    }

    public void registrarDatos()
    {
        //La key del map deben ser los nombres de los campos en la tabla
        Map<String, String> datos = new HashMap<String, String>();
        datos.put("nombre",nombreYApellido);
        datos.put("mail",mail);
        datos.put("dni",dni);
        datos.put("contrasena",pass);

        String result = Conexion.enviarPost(datos, PHP_NAME_REGISTRADOR);

        //TODO: qué hago con el result?
        Log.d("result post regisdatos", result);

    }


}
