package com.pps1.guiame.guiame.utils;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;

import com.pps1.guiame.guiame.dto.Curso;

public class Utils
{

    public static String ubicacionAula(String aula)
    {
        if(aula == null || aula.isEmpty()){
            return "Numero aula no disponible";
        }

        String ubicacion = "";

        //Si no tiene número al ppio, asumo que es una del estilo "LABO 7012"
        if(Character.isLetter(aula.charAt(0)))
        {
            //Veo cada palabra si alguna es numérica
            String[] nombreAula = aula.split(" ");
            for(String N : nombreAula)
            {
                boolean isDigito = true;
                for(int i = 0; i<N.length(); i++)
                {
                    //Si tiene alguna letra, paso a la próxima palabra
                    if(Character.isLetter(N.charAt(i)))
                    {
                        isDigito = false;
                        break;
                    }

                    //
                    if(Character.isDigit(N.charAt(i)))
                        continue;

                }
                if(isDigito)
                {
                    aula = N;
                    break;
                }
            }
        //Si sigue teniendo letra, devuelvo lo que tenga
        if(Character.isLetter(aula.charAt(0)))
            return aula;
        }

        //Si el primer digito es numerico, entonces es el numeor del modulo
        if(Character.isDigit(aula.charAt(0)))
        {
                ubicacion = "Modulo " + aula.charAt(0);
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

        Log.d("aulaDevuelve", ubicacion);
        return ubicacion;
    }

    public static void verDetallesCurso(Curso curso, AlertDialog.Builder builder)
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

    public static void verDetallesInfo(String titulo, String info, AlertDialog.Builder builder)
    {
        builder.setTitle(titulo);
        builder.setMessage(info);
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
