package com.pps1.guiame.guiame.utils;

/**
 * Created by javi on 29/04/15.
 */
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

    /**
     * Devuelve el número del aula dentro del texto que recibe como parámetro.
     *
     * @param itemSeleccionado debe ser algo como "PPS1 - Aula:7070 - COM - DIA HORARIO - PROF"
     */
    public static String getNumAula(String itemSeleccionado)
    {
        String aula = "";
        if(itemSeleccionado != null && !itemSeleccionado.isEmpty()) {
            int posInicio = itemSeleccionado.indexOf(":");
            int posFin = itemSeleccionado.indexOf("-", posInicio);
            try
            {
                aula = itemSeleccionado.substring(posInicio + 1, posFin).trim();
            } catch (Exception e)
            {
                aula = "Sin Aula";
            }
            return aula;
        }
        return aula;
    }
}
