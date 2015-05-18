package com.pps1.guiame.guiame.controlador;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.pps1.guiame.guiame.R;
import com.pps1.guiame.guiame.dto.Aula;
import com.pps1.guiame.guiame.dto.Curso;
import com.pps1.guiame.guiame.persistencia.dao.AulaDAO;
import com.pps1.guiame.guiame.persistencia.dao.Borrador;
import com.pps1.guiame.guiame.persistencia.dao.Listador;

import java.util.ArrayList;


public class ListaCursos extends ActionBarActivity
{
    private ListView listaCursos;
    ArrayAdapter<Curso> adaptador;
    EditText searchBox;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_cursos);
        // as before
        listaCursos = (ListView) findViewById(R.id.listaMaterias);
        registerForContextMenu(listaCursos);
        listaCursos.setAdapter(adaptador);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); //Escondemos el teclado
        String nombreUsuario = UsuarioLogin.getNombre() != null ? " "+ UsuarioLogin.getNombre() : "";
        setTitle(this.getString(R.string.title_activity_lista)+nombreUsuario+"!");
        dialog = new ProgressDialog(this);

        Thread tr = new Thread()
        {
            @Override
            public void run(){
                final ArrayList<Curso> cursos = (ArrayList<Curso>) getIntent().getExtras().get("Cursos");
                getIntent().getExtras().clear();
                runOnUiThread(
                        new Runnable() {
                            @Override
                            public void run()
                            {
                               mostrarItems(cursos);
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
                if(adaptador == null)
                {
                    return;
                }
                // When user changed the Text
                ListaCursos.this.adaptador.getFilter().filter(cs);
            }

            @Override
            public void afterTextChanged(Editable editable){}

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3){}
        });
    }

    /*
     * Si el usuario está logeado, desactivamos las opciones de Iniciar sesión y Registrarse del menu
     * y activamos Agregar nuevo curso y Borrar curso
     * Si el usuario no está logeado, desactivar la opción de Salir
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_lista, menu);
        if(UsuarioLogin.isUserOn())
        {
            menu.getItem(0).setVisible(false); //Iniciar Sesion
            menu.getItem(1).setVisible(false); //Registrarse
            menu.getItem(2).setVisible(true);  //Agregar nuevo curso
        }
        else
        {
            menu.getItem(3).setVisible(false); //Salir
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
            Intent intent = new Intent(ListaCursos.this, Ingreso.class);
            startActivity(intent);
        }
        else if(id == R.id.Registrarse)
        {
            Intent intent = new Intent(ListaCursos.this, Registro.class);
            startActivity(intent);
        }
        else if(id == R.id.AgregarNuevoCurso)
        {
            Intent intent = new Intent(getApplicationContext(), CursoPersonalizado.class);
            startActivity(intent);
            finish();
        }
        else if (id == R.id.Salir)
        {
            UsuarioLogin.logout();
            Toast.makeText(this, "Ha cerrado sesión", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ListaCursos.this, Principal.class);
            startActivity(intent);
        }
        finish();
        return super.onOptionsItemSelected(item);
    }
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_opciones_lista, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
                .getMenuInfo();
        switch (item.getItemId())
        {
            case R.id.menuBorrarCurso:
                this.eliminarCurso(info.position);
                this.llenarLista();
                return true;
            case R.id.menuLocalizarCurso:
                this.geoLocalizarAula(info.position);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    public void mostrarItems(ArrayList<Curso> datos)
    {
        adaptador = new ArrayAdapter<Curso>(this,android.R.layout.simple_list_item_1,datos);
        listaCursos.setAdapter(adaptador);
    }


    private void eliminarCurso(int posicionItemSeleccionado)
    {
        final Curso curso = (Curso) listaCursos.getItemAtPosition(posicionItemSeleccionado);
        dialog.setMessage("Eliminando curso...");
        dialog.show();
        Thread tr = new Thread()
        {
            String idCurso = Integer.toString(curso.getId());
            String idUsuario = Integer.toString(UsuarioLogin.getId());

            @Override
            public void run()
            {
                String nombreMateria = curso.getNombre();
                Borrador borrador = new Borrador(idCurso,idUsuario,nombreMateria);
                borrador.eliminarCurso();
                llenarLista(); //Llamo dos veces, esta mal!
            }
        };
        tr.start();
    }

    private void geoLocalizarAula(int posicionItemSeleccionado)
    {
        final Curso itemSeleccionado = (Curso) listaCursos.getAdapter().getItem(posicionItemSeleccionado);
        dialog.setMessage("Buscando aula...");
        dialog.show();
        Thread tr = new Thread()
        {
            @Override
            public void run()
            {
                Aula aula = new AulaDAO().getAula(itemSeleccionado.getAula());
                if(aula == null)
                {
                    runOnUiThread(
                            new Runnable()
                            {
                                @Override
                                public void run()
                                {
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

    private void llenarLista()
    {
        Thread tr = new Thread()
        {
            @Override
            public void run()
            {
                Listador listador = new Listador(UsuarioLogin.getId());
                final ArrayList<Curso> cursos = listador.getListadoCursosUsuario();
                runOnUiThread(
                        new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                mostrarItems(cursos);
                                dialog.dismiss(); //Cierra el dialog de EliminarCurso
                                Toast.makeText(getApplicationContext(),
                                        "Se elimino el curso correctamente", Toast.LENGTH_LONG).show();
                            }
                        });
            }
        };
        tr.start();
    }

    //Al presionar el botón Atrás vuelve a la clase Principal
    @Override
    public void onBackPressed()
    {
        Intent start = new Intent(ListaCursos.this,Principal.class);
        startActivity(start);
        finishActivity(0);
    }
}
