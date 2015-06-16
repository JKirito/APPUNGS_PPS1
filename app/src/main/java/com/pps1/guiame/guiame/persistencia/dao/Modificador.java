package com.pps1.guiame.guiame.persistencia.dao;

import android.util.Log;

import com.pps1.guiame.guiame.controlador.Perfil;
import com.pps1.guiame.guiame.persistencia.conexion.Conexion;

import java.util.HashMap;
import java.util.Map;

public class Modificador {
    private final String PHP_NAME_MODIFICADOR = "modificarUsuario.php";

    private String nombreYApellido;
    private String dni;
    private String mail;
    private String pass;
    private String pass2;

    public Modificador(String nombreYApellido, String dni, String mail, String pass, String pass2)
    {
        this.nombreYApellido = nombreYApellido;
        this.dni = dni;
        this.mail = mail;
        this.pass = pass;
        this.pass2 = pass2;
    }

    public void actualizarDatos() throws Exception {
        //La key del map deben ser los nombres de los campos en la tabla
        Map<String, String> datos = new HashMap<String, String>();
        datos.put("id", Integer.toString(Perfil.getId()));
        datos.put("nombre", nombreYApellido);
        datos.put("mail", mail);
        datos.put("dni", dni);
        datos.put("contrasena", pass);

        Log.d("mail!!!!!!", mail);
        Log.d("dni!!!!!!", dni);
        Log.d("contrasena!!!!!!", pass);

        String result = Conexion.enviarPost(datos, PHP_NAME_MODIFICADOR);
    }
}
