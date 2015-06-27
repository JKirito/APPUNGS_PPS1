package com.pps1.guiame.guiame.persistencia.dao;


import com.pps1.guiame.guiame.controlador.Perfil;
import com.pps1.guiame.guiame.persistencia.conexion.Conexion;
import com.pps1.guiame.guiame.utils.ValidadorMail;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Verificador
{
    private static final String MSJ_NOM_APELLINVALIDO = "Nombre y Apellido inválido";
    private static final String MSJ_NOM_APELL_CORTO = "Nombre y Apellido demasiado corto";
    private static final String MSJ_DNI_INVALIDO = "DNI invalido";
    private static final String MSJ_DNI_INVALIDO_SOLONUM = "El DNI sólo puede contener números";
    private static final String MSJ_MAIL_INVALIDO = "Mail invalido";
    private static final String MSJ_PASS_CORTA = "La contraseña debe tener al menos 4 caracteres";
    private static final String MSJ_PASS_NULL = "Debe ingresar la contraseña";
    private static final String MSJ_PASS_NOCOINCIDE = "Las contraseñas no coinciden";

    private static final String PHP_NAME_VERIFICARUSUARIO = "verificarUsuario.php";

    private static String nombreYApellido;
    private static String dni;
    private static String mail;
    private static String pass;
    private static String pass2;

    public Verificador(String nombreYApellido, String dni, String mail, String pass, String pass2)
    {
        this.nombreYApellido = nombreYApellido;
        this.dni = dni;
        this.mail = mail;
        this.pass = pass;
        this.pass2 = pass2;
    }

    public Verificador(String dni, String pass)
    {
        this.dni = dni;
        this.pass = pass;
    }

    public static List<String> validarDatosRegistro() throws Exception {
        List<String> errores = new ArrayList<String>();
        if(nombreYApellido == null || nombreYApellido.replaceAll("\\s","").length() == 0)
        {
            errores.add(MSJ_NOM_APELLINVALIDO);
        }
        if(nombreYApellido != null && nombreYApellido.replaceAll("\\s","").length() <= 5)
        {
            errores.add(MSJ_NOM_APELL_CORTO);
        }
        if(dni == null || dni.length() != 8)
        {
            errores.add(MSJ_DNI_INVALIDO);
        }

        try
        {
            new Integer(dni);
        }
        catch (Exception e)
        {
            errores.add(MSJ_DNI_INVALIDO_SOLONUM);
        }

        if(mail == null || !ValidadorMail.validarEmail(mail))
        {
            errores.add(MSJ_MAIL_INVALIDO);
        }

        if(pass == null)
        {
            errores.add(MSJ_PASS_NULL);
        }
        else
        {
            if(!pass.equals(pass2))
            {
                errores.add(MSJ_PASS_NOCOINCIDE);
            }else if(pass.length()<4)
            {
                errores.add(MSJ_PASS_CORTA);
            }
        }

        if(!Perfil.isUserOn() || !Perfil.getUsuario().equals(dni))
        {
            //Verifico que no exista un USUARIO con el dni ingresado
            //Solo verifico si no hubo errores aun, por temas de performance
            if(errores.size() == 0)
            {
                Map<String, String> datos = new HashMap<String, String>();
                datos.put("dni",dni);
                datos.put("mail",mail);
                String result = Conexion.enviarPost(datos, PHP_NAME_VERIFICARUSUARIO);
                if(isUsuarioExistente(result))
                {
                    errores.add("Ya existe un usuario con ese dni o mail");
                }
            }
        }

        return errores;
    }

    public static Boolean isUsuarioExistente(String response)
    {
        Boolean existe = false;
        try
        {
            JSONArray json= new JSONArray(response);
            String cantidadRegistrados="";
            cantidadRegistrados = json.getJSONObject(0).getString("COUNT(*)");

            if( cantidadRegistrados.toString().equals("1"))//Si hay un registrado que tiene ese dni
            {                                               //y contraseña, entonces es valido
                existe = true;
            }
            else
            {
                existe = false;
            }
        }
        catch (Exception e)
        {
            // TODO: handle exception
        }
        return existe;
    }

    public static List<String> validarDatosIngresados()
    {
        List<String> errores = new ArrayList<String>();
        if(dni == null || dni.length() != 8)
        {
            errores.add(MSJ_DNI_INVALIDO);
        }

        if(pass == null)
        {
            errores.add(MSJ_PASS_NULL);
        }
        return errores;
    }

    public static List<String> validarUsuario(String resultadoJSON)
    {
        List<String> errores = validarDatosIngresados();
        if(errores.size() > 0)
        {
            return errores;
        }

        Boolean isUsuarioValido = isUsuarioExistente(resultadoJSON);
        if(!isUsuarioValido)
            errores.add("Usuario inválido. Revise sus datos");

        if(errores.size() > 0)
        {
            return errores;
        }

        return errores;
    }
}
