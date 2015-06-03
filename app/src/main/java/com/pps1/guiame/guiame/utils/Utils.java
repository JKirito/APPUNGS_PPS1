package com.pps1.guiame.guiame.utils;

import android.app.AlertDialog;
import android.content.DialogInterface;

import com.pps1.guiame.guiame.dto.Curso;

public class Utils
{

    public static String ubicacionAula(String aula)
    {
        if(aula == null || aula.isEmpty()){
            return "Numero aula no disponible";
        }

        String ubicacion = "";
        Boolean numAsignado= false;
        int i = 1; //No es la posicion sino el entero
        while(!numAsignado)
        {
            if(aula.startsWith(String.valueOf(i))) //Si el aula empieza con el numero... (1 al 7)
            {
                ubicacion = "Modulo " + i;
                numAsignado = true;
            }
            i++;
        }

        if(aula.charAt(1) == '0')
        {
            ubicacion+= " en planta baja";
        }
        else if(aula.charAt(1) == '1')
        {
            ubicacion+= " en el primer piso";
        }
        else if(aula.charAt(1) == '2')
        {
            ubicacion += " en el segundo piso";
        }
        //Si no tiene número, asumo que es una del estilo "oficina docente"
        else if(Character.isLetter(aula.charAt(0)))
        {
            ubicacion = aula;
        }

        return ubicacion;
    }

    public static void verDetalles(Curso curso, AlertDialog.Builder builder)
    {
        builder.setTitle("Detalles");
        builder.setMessage("Prof.:" + curso.getDocente());
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
                dialog.dismiss();
            }
        });
        builder.show();
    }


}
