package com.pps1.guiame.guiame;

/**
 * Created by Agustina on 29/03/2015.
 */

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import org.json.JSONArray;

import java.util.ArrayList;


public class ListaMateriasUsuario extends ActionBarActivity
{
    private final String PHP_NAME_LISTADOR = "listarMateriasUsuario.php";
    private ListView listaMaterias;
    ArrayAdapter<String> adaptador;
    EditText searchBox;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_materias);
        Thread tr = new Thread()
        {
            @Override
            public void run(){
                final ArrayList<String> materias = (ArrayList<String>) getIntent().getExtras().get("Materias");
                getIntent().getExtras().clear();
                runOnUiThread(
                        new Runnable() {
                            @Override
                            public void run() {
                                cargaListado(materias);
                            }
                        });
            }
        };
        tr.start();
        searchBox = (EditText) findViewById(R.id.searchBox);
        searchBox.addTextChangedListener(new TextWatcher()
        {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3)
            {
                // When user changed the Text
                ListaMateriasUsuario.this.adaptador.getFilter().filter(cs);
            }

            @Override
            public void afterTextChanged(Editable editable)
            {

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3)
            {
                // TODO Auto-generated method stub

            }
        });

    }

    public ArrayList<String> obtDatosJSON(String response)
    {
        ArrayList<String> listado= new ArrayList<String>();
        try {
            JSONArray json= new JSONArray(response);
            String texto;
            for (int i=0; i<json.length();i++)
            {
                texto = json.getJSONObject(i).getString("alias") +" - "+
                        json.getJSONObject(i).getString("comision") +" - "+
                        json.getJSONObject(i).getString("dia") +" de "+
                        json.getJSONObject(i).getString("horaInicio") +" a "+
                        json.getJSONObject(i).getString("horaFin") +" - "+
                        json.getJSONObject(i).getString("nombre");
                Log.d("texto", texto);
                listado.add(texto);
            }
        }
        catch (Exception e)
        {
            Log.d("EXCEPCION obtDatosJSON", e+"");
        }
        return listado;
    }

    public void cargaListado(ArrayList<String> datos)
    {
        adaptador = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,datos);
        listaMaterias = (ListView) findViewById(R.id.listaMaterias);
        listaMaterias.setAdapter(adaptador);
    }
}
