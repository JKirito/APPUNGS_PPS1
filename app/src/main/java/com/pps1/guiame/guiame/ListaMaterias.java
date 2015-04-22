package com.pps1.guiame.guiame;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import java.util.ArrayList;

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
            final String itemSeleccionado = listaMaterias.getItemAtPosition(posicion).toString();

            Thread tr = new Thread()
            {
                @Override
                public void run()
                {
                    BuscadorAula buscadorAula = new BuscadorAula(itemSeleccionado);

                       if(buscadorAula.getAula() == null)
                       {
                           runOnUiThread(
                                   new Runnable() {
                                       @Override
                                       public void run() {
                                           Toast.makeText(getApplicationContext(),
                                                   "No se ha podido localizar el aula =(", Toast.LENGTH_SHORT).show();
                                       }
                                   });
                           return;
                       }

                    Bundle bundleBuscAula = new Bundle();
                    bundleBuscAula.putSerializable("Aula", buscadorAula.getAula());

                    Intent intent = new Intent(getApplicationContext(), Mapa.class);
                    intent.putExtras(bundleBuscAula);
                    startActivity(intent);


                }
            };
            tr.start();
        }
    }

  /*  protected void onPostExecute(String message)
    {
        pDialog.dismiss();
    }

    // @Override
    protected void onPreExecute()
    {
        super.onPreExecute();
        pDialog = new ProgressDialog(ListaMaterias.this);
        pDialog.setMessage("Attempting for login...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();
    }*/
    //Esto tambien deberia ir a Listador pero no se puede pasar el ListView
    public void mostrarItems(ArrayList<String> datos)
    {
        adaptador = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,datos);
        listaMaterias = (ListView) findViewById(R.id.listaMaterias);
        listaMaterias.setAdapter(adaptador);
    }
}
