package com.pps1.guiame.guiame.persistencia.dao;

import com.pps1.guiame.guiame.persistencia.conexion.Utils;
import com.pps1.guiame.guiame.dto.Aula;

import java.util.HashMap;
import java.util.Map;

public class AgregadorAula
{
    private String numeroAula;
    private String ubicacion;
    private final String PHP_NAME_REGISTRADOR_AULA = "registrarAula.php";

    public AgregadorAula(Aula aula)
    {
        this.numeroAula = aula.getNumAula();
        this.ubicacion = aula.getLatitud()+","+aula.getLongitud();
    }

    public void registrarAula()
    {
        //La key del map deben ser los nombres de los campos en la tabla
        Map<String, String> datos = new HashMap<String, String>();
        datos.put("numero",this.numeroAula);
        datos.put("ubicacion",this.ubicacion);

        String result = Utils.enviarPost(datos, PHP_NAME_REGISTRADOR_AULA);
    }
}
