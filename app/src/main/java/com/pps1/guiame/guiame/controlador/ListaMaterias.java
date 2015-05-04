package com.pps1.guiame.guiame.controlador;

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

import com.pps1.guiame.guiame.R;
import com.pps1.guiame.guiame.dto.Aula;
import com.pps1.guiame.guiame.persistencia.dao.AulaDAO;
import com.pps1.guiame.guiame.utils.Utils;

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
        String nombreUsuario = SessionManager.getNombre() != null ? " "+SessionManager.getNombre() : "";
        setTitle(this.getString(R.string.title_activity_lista)+nombreUsuario+"!");
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

    /*
     * Si el usuario está logeado, desactivamos las opciones de Iniciar sesión y Registrarse del menu
     * Si el usuario no está logeado, desactivar la opción de Salir
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_lista, menu);
        if(SessionManager.isUserOn())
        {
            menu.getItem(0).setVisible(false); //Iniciar Sesion
            menu.getItem(1).setVisible(false); //Registrarse
        }
        else
        {
            menu.getItem(2).setVisible(false); //Salir
        }
        return true;
    }

    /* Si se eligió la opción:
      "IniciarSesion", vamos a Ingreso
      "Registrarse", vamos a Registro
      "Salir", cerramos la sesión actual y vuelve a Principal
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if(id == R.id.IniciarSesion)
        {
            Intent intent = new Intent(ListaMaterias.this, Ingreso.class);
            startActivity(intent);
        }
        else if(id == R.id.Registrarse)
        {
            Intent intent = new Intent(ListaMaterias.this, Registro.class);
            startActivity(intent);
        }
        else if (id == R.id.Salir)
        {
            SessionManager.logout();
            Toast.makeText(this, "Ha cerrado sesión", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ListaMaterias.this, Principal.class);
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
                    String numAula = Utils.getNumAula(itemSeleccionado);
                    Aula aula = new AulaDAO().getAula(numAula);

                       if(aula == null)
                       {
                           runOnUiThread(
                                   new Runnable() {
                                       @Override
                                       public void run() {
                                           Toast.makeText(getApplicationContext(),
                                                   "No se ha podido localizar el aula", Toast.LENGTH_SHORT).show();
                                           dialog.dismiss();
                                       }
                                   });
                           return;
                       }

                    Bundle bundleBuscAula = new Bundle();
                    bundleBuscAula.putSerializable("Aula", aula);

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
