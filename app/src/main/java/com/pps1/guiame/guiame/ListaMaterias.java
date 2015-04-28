package com.pps1.guiame.guiame;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class ListaMaterias extends ActionBarActivity
{
    private ListView listaMaterias;
    ArrayAdapter<String> adaptador;
    EditText searchBox;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_materias);
        dialog = new ProgressDialog(this);

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
                if(adaptador == null){
                    return;
                }
                Log.d("adapter","adaptador"+adaptador);
                Log.d("adapter","adaptadorFiltro"+adaptador.getFilter());

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_lista, menu);
        //Verificamos si el usuario está logeado
        if(SessionManager.isUserOn())
        {
            menu.getItem(0).setVisible(false); //Si el usuario está logeado, desactivar la opción de Iniciar sesión
        }
        else
        {
            menu.getItem(1).setVisible(false); //Si el usuario no está logeado, desactivar la opción de Salir
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        //Si se elige Salir, mandamos al usuario a la pantalla principal
        if (id == R.id.Logout)
        {
            SessionManager.logout();
            Toast.makeText(this, "Usuario Deslogeado", Toast.LENGTH_SHORT).show();
            Intent intent =
                    new Intent(ListaMaterias.this, Principal.class);
            startActivity(intent);
        }
        else if(id == R.id.IniciarSesion)
        {
            //Si se elige iniciar sesión, mandamos al usuario a Ingreso para que ponga su cuenta
            Intent intent =
                    new Intent(ListaMaterias.this, Ingreso.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
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
            dialog.setMessage("Buscando Aula...");
            dialog.show();
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
                                           dialog.dismiss();
                                       }
                                   });
                           return;
                       }

                    Bundle bundleBuscAula = new Bundle();
                    bundleBuscAula.putSerializable("Aula", buscadorAula.getAula());

                    Intent intent = new Intent(getApplicationContext(), Mapa.class);
                    intent.putExtras(bundleBuscAula);
                    startActivity(intent);
                    dialog.dismiss();
                }
            };
            tr.start();
        }
    }

    //Esto tambien deberia ir a Listador pero no se puede pasar el ListView
    public void mostrarItems(ArrayList<String> datos)
    {
        adaptador = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,datos);
        listaMaterias = (ListView) findViewById(R.id.listaMaterias);
        listaMaterias.setAdapter(adaptador);
    }

    @Override
    protected void onResume()
    {
        ArrayAdapter<String> a = adaptador;
        super.onResume();
        adaptador = a;
    }
}
