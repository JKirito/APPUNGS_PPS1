package com.pps1.guiame.guiame.controlador;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import com.pps1.guiame.guiame.persistencia.dao.CursoDAO;
import com.pps1.guiame.guiame.utils.Utils;

import java.io.IOException;
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
        listaCursos = (ListView) findViewById(R.id.listaMaterias);
        registerForContextMenu(listaCursos);
        listaCursos.setAdapter(adaptador);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); //Escondemos el teclado
        String nombreUsuario = Perfil.getNombre() != null ? " " + Perfil.getNombre() : "";
        setTitle(this.getString(R.string.title_activity_lista) + nombreUsuario + "!");
        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

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
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                if (adaptador == null) {
                    return;
                }
                // When user changed the Text
                ListaCursos.this.adaptador.getFilter().filter(cs);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
            }
        });

        ((ListView) findViewById(R.id.listaMaterias)).setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                geoLocalizarAula(position);
            }
        });
    }

    /*
     * Si el usuario está logeado, desactivamos las opciones de Iniciar sesión y Registrarse del menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_lista, menu);
        if (Perfil.isUserOn())
        {
            menu.getItem(0).setVisible(false); //Iniciar Sesion
            menu.getItem(1).setVisible(false); //Registrarse
        }
        return true;
    }


    /* Si se eligió la opción:
      "IniciarSesion", vamos a Ingreso
      "Registrarse", vamos a Registro
      "Salir", cerramos la sesión actual y vuelve a Principal
      "Agregar nuevo curso", vamos a CursoPersonalizado
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (id == R.id.IniciarSesion)
        {
            Intent intent = new Intent(ListaCursos.this, Ingreso.class);
            startActivity(intent);
        }
        else if (id == R.id.Registrarse)
        {
            Intent intent = new Intent(ListaCursos.this, Registro.class);
            startActivity(intent);
        }
        else if (id == R.id.AgregarNuevoCurso)
        {
            Bundle bundleAgregarCurso = new Bundle();
            bundleAgregarCurso.putBoolean("AgregarCurso", true);

            Intent intent = new Intent(getApplicationContext(), CursoPersonalizado.class);
            intent.putExtras(bundleAgregarCurso);
            startActivity(intent);
            finish();
        }
        else if(id==R.id.ModificarPerfil)
        {
            Intent intent = new Intent(getApplicationContext(), Registro.class);
            startActivity(intent);
            finish();
        }
        else if (id == R.id.Salir)
        {
            Perfil.logout();
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
        Log.d("detalles", "rrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
                .getMenuInfo();
        Log.d("detalles", "CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC");
        switch (item.getItemId())
        {
            case R.id.menuBorrarCurso:
                preguntar(info.position);
                return true;
            case R.id.menuVerDetalles:
                AlertDialog.Builder builder = new AlertDialog.Builder(ListaCursos.this);
                final Curso curso = (Curso) listaCursos.getItemAtPosition(info.position);
                Utils.verDetalles(curso, builder);
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


    private void eliminarCurso(int posItemSeleccionado)
    {
        final int posicionItemSeleccionado = posItemSeleccionado;
        final Curso curso = (Curso) listaCursos.getItemAtPosition(posicionItemSeleccionado);
        dialog.setMessage("Eliminando curso...");
        dialog.show();
        Thread tr = new Thread()
        {
            Integer idCurso = curso.getId();

            @Override
            public void run()
            {
                try {
                    new CursoDAO().eliminarCurso(curso, Perfil.getId());
                } catch (IOException e) {
                    e.printStackTrace();
                    runOnUiThread(
                            new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(),
                                            "Hubo un error al eliminar el Curso, intente de nuevo.", Toast.LENGTH_SHORT).show();
                                }
                            });
                    return;
                }
                runOnUiThread(
                        new Runnable() {
                            @Override
                            public void run() {
                                ArrayList<Curso> cursosAeliminar = new ArrayList<Curso>();
                                for (int i = 0; i < adaptador.getCount(); i++) {
                                    if (((Curso) adaptador.getItem(i)).getId().equals(idCurso)) {
                                        cursosAeliminar.add(adaptador.getItem(i));
                                    }
                                }

                                for (Curso c : cursosAeliminar) {
                                    adaptador.remove(c);
                                }
                                adaptador.notifyDataSetChanged();
                                dialog.dismiss(); //Cierra el dialog de EliminarCurso
                                Toast.makeText(getApplicationContext(),
                                        "Se elimino el curso correctamente", Toast.LENGTH_LONG).show();
                            }
                        });
            }
        };
        tr.start();
    }

    private void geoLocalizarAula(int posicionItemSeleccionado)
    {
        final Curso itemSeleccionado = (Curso) listaCursos.getAdapter().getItem(posicionItemSeleccionado);
        dialog.setMessage("Buscando aula...");
        dialog.show();
        Thread tr = new Thread() {
            @Override
            public void run() {
                final Aula aula;
                try {
                    aula = new AulaDAO().getAula(itemSeleccionado.getAula());
                } catch (IOException e) {
                    e.printStackTrace();
                    runOnUiThread(
                            new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(),
                                            "Hubo un error al localizar el aula, intente de nuevo.", Toast.LENGTH_SHORT).show();
                                }
                            });
                    return;
                }
                if(aula == null || aula.getLatitud() == null || aula.getLongitud() == null)
                {
                    runOnUiThread(
                            new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    String msj = aula == null ? "No se ha podido localizar el aula" : "No se tiene su ubicación :(";
                                    Toast.makeText(getApplicationContext(), msj, Toast.LENGTH_SHORT).show();
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
                //finish();
            }
        };
        tr.start();
    }

    private void preguntar(int posicionCurso)
    {
        final int posicionCursoABorrar = posicionCurso;
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                switch (which)
                {
                    case DialogInterface.BUTTON_POSITIVE:
                        eliminarCurso(posicionCursoABorrar);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No borramos nada
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(ListaCursos.this);
        builder.setMessage("¿Desea borrar el curso?").setPositiveButton("Si", dialogClickListener).setNegativeButton("No", dialogClickListener).show();
    }

    //Al presionar el botón Atrás vuelve a la clase Principal
    @Override
    public void onBackPressed() {
        Intent start = new Intent(ListaCursos.this, Principal.class);
        startActivity(start);
        finish();
    }

}
