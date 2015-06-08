package com.pps1.guiame.guiame.controlador;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.pps1.guiame.guiame.R;
import com.pps1.guiame.guiame.dto.Aula;
import com.pps1.guiame.guiame.dto.Curso;
import com.pps1.guiame.guiame.persistencia.dao.AulaDAO;
import com.pps1.guiame.guiame.persistencia.dao.Listador;
import com.pps1.guiame.guiame.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;

public class CursoPersonalizado extends Activity
{
    private Button btnBuscarCurso;
    private ListView listaCursosJuntos;
    private EditText txtNombreMateria;
    ArrayAdapter<Curso> adaptadorCursos;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_curso_personalizado);

        listaCursosJuntos = (ListView) findViewById(R.id.listaCursosJuntos);
        registerForContextMenu(listaCursosJuntos);
        listaCursosJuntos.setAdapter(adaptadorCursos);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); //Escondemos el teclado

        btnBuscarCurso = (Button)findViewById(R.id.btnBuscarCurso);
        txtNombreMateria = (EditText)findViewById(R.id.txtMateriaNombre);
        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        final boolean agregandoCurso = getIntent().getBooleanExtra("AgregarCurso", false);
        Log.d("agregarCurso", agregandoCurso + "");

        btnBuscarCurso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String nombreMat = txtNombreMateria.getText().toString().trim();

                if (nombreMat.trim().length() < 4) {
                    Toast.makeText(getApplicationContext(),
                            "Debe ingresar al menos 4 caracteres", Toast.LENGTH_SHORT).show();
                    return;
                }

                dialog.setMessage("Buscando...");
                dialog.show();

                Thread tr = new Thread() {
                    @Override
                    public void run() {
                        final ArrayList<Curso> cursos;
                        Listador listador = new Listador(Perfil.getId(), nombreMat);
                        try {
                            if (Perfil.isUserOn() && agregandoCurso)
                            {
                                cursos = listador.getListadoCursosJuntos();
                            }
                            else
                            {
                                cursos = listador.getListadoCursos();
                            }


                        } catch (IOException e) {
                            e.printStackTrace();
                            runOnUiThread(
                                    new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getApplicationContext(),
                                                    "Hubo un error al obtener los Cursos, intente de nuevo.", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                            return;
                        }
                        runOnUiThread(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        if (cursos == null || cursos.isEmpty()) {
                                            Toast.makeText(getApplicationContext(),
                                                    "No se han encontrado cursos. Pruebe escribir otro nombre", Toast.LENGTH_LONG).show();
                                            return;
                                        }
                                        mostrarItems(cursos);
                                    }
                                });

                        dialog.dismiss();
                    }
                };
                tr.start();
            }
        });

        // Registra callback si se selecciona un item de este AdaptView
        ((ListView) findViewById(R.id.listaCursosJuntos)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (Perfil.isUserOn() && agregandoCurso) {
                    final Curso cursoSeleccionado = (Curso) listaCursosJuntos.getAdapter().getItem(position);

                    Thread tr = new Thread() {
                        @Override
                        public void run() {
                            Bundle bundleNombrePersonalizado = new Bundle();
                            bundleNombrePersonalizado.putSerializable("Curso", cursoSeleccionado);

                            Intent intent = new Intent(getApplicationContext(), NombreCursoPersonalizado.class);
                            intent.putExtras(bundleNombrePersonalizado);
                            startActivity(intent);
                            //finish();
                        }
                    };
                    tr.start();
                } else
                {
                    geoLocalizarAula(position);
                }

        }
        });

    }
    public void mostrarItems(ArrayList<Curso> datos)
    {
        adaptadorCursos = new ArrayAdapter<Curso>(this,android.R.layout.simple_list_item_1,datos);
        listaCursosJuntos.setAdapter(adaptadorCursos);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
                .getMenuInfo();
        switch (item.getItemId())
        {
            case R.id.menuVerDetalles:
                AlertDialog.Builder builder = new AlertDialog.Builder(CursoPersonalizado.this);
                final Curso curso = (Curso) listaCursosJuntos.getItemAtPosition(info.position);
                Utils.verDetalles(curso, builder);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }


    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_solo_ver_detalles, menu);
    }

    private void geoLocalizarAula(int posicionItemSeleccionado)
    {
        final Curso itemSeleccionado = (Curso) listaCursosJuntos.getAdapter().getItem(posicionItemSeleccionado);
        dialog.setMessage("Buscando aula...");
        dialog.show();
        Thread tr = new Thread()
        {
            @Override
            public void run()
            {
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
                                            "Hubo un error al obtener el aula, intente de nuevo.", Toast.LENGTH_SHORT).show();
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

    public void onBackPressed()
    {
        Intent start = new Intent(CursoPersonalizado.this,Principal.class);
        startActivity(start);
        finish();
    }
}
