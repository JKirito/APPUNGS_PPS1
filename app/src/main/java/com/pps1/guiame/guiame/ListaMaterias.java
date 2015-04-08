package com.pps1.guiame.guiame;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.view.View.OnClickListener;
import java.util.ArrayList;


public class ListaMaterias extends ActionBarActivity
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


    //Esto tambien deberia ir a Listador pero no se puede pasar el ListView
    public void mostrarItems(ArrayList<String> datos)
    {
        adaptador = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,datos);
        listaMaterias = (ListView) findViewById(R.id.listaMaterias);
        listaMaterias.setAdapter(adaptador);

    }
}
