package com.pps1.guiame.guiame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import java.util.ArrayList;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.Toast;


public class ListaMaterias extends Activity
{
    private ListView listaMaterias;
    ArrayAdapter<String> adaptador;
    EditText searchBox;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_materias);
        final Listador listador = new Listador();
        Thread tr = new Thread()
        {
            @Override
            public void run(){
                final ArrayList<String> materias = listador.getListadoMaterias();
                runOnUiThread(
                        new Runnable() {
                            @Override
                            public void run() {
                                mostrarItems(materias);
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
                ListaMaterias.this.adaptador.getFilter().filter(cs);
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

    public void getSelected(View view)
    {
        int posicion = listaMaterias.getCheckedItemPosition(); //Devuelve la posición del item seleccionado
        if(posicion<0)
        {
            //Si no hay elemento seleccionado...
            Toast.makeText(this, "No hay elementos seleccionados", Toast.LENGTH_SHORT).show();
        }
        else
        {
            String itemSeleccionado = listaMaterias.getItemAtPosition(posicion).toString();
            int posInicio = itemSeleccionado.indexOf(":");
            int posFin = itemSeleccionado.indexOf("-", posInicio);
            String aula = itemSeleccionado.substring(posInicio+1,posFin).trim();

            Toast.makeText(this, "AULA " +  aula, Toast.LENGTH_SHORT).show();

            Bundle b = new Bundle();
            b.putString("elementoSeleccionado", aula);

            Intent intent = new Intent(getApplicationContext(), Prueba.class);
            intent.putExtras(b);

            startActivity(intent);
        }
    }

    //Esto tambien deberia ir a Listador pero no se puede pasar el ListView
    public void mostrarItems(ArrayList<String> datos)
    {
        adaptador = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,datos);
        listaMaterias = (ListView) findViewById(R.id.listaMaterias);
        listaMaterias.setAdapter(adaptador);
    }
}
