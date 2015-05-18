package com.pps1.guiame.guiame.controlador;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); //Escondemos el teclado
        btnBuscarCurso = (Button)findViewById(R.id.btnBuscarCurso);
        txtNombreMateria = (EditText)findViewById(R.id.txtMateriaNombre);
        dialog = new ProgressDialog(this);

        btnBuscarCurso.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final String nombreMat = txtNombreMateria.getText().toString().trim();

                if(nombreMat.trim().length() < 4)
                {
                    Toast.makeText(getApplicationContext(),
                            "Debe ingresar al menos 4 caracteres", Toast.LENGTH_SHORT).show();
                    return;
                }

                dialog.setMessage("Buscando...");
                dialog.show();

                Thread tr = new Thread()
                {
                    @Override
                    public void run()
                    {
                        final ArrayList<Curso> cursos;
                        if(UsuarioLogin.isUserOn())
                        {
                            Listador listador = new Listador(UsuarioLogin.getId(), nombreMat);
                            cursos = listador.getListadoCursosJuntos();
                            runOnUiThread(
                                    new Runnable()
                                    {
                                        @Override
                                        public void run()
                                        {
                                            if(cursos == null || cursos.isEmpty())
                                            {
                                                Toast.makeText(getApplicationContext(),
                                                        "No se han encontrado cursos. Pruebe escribir otro nombre", Toast.LENGTH_LONG).show();
                                            }


                                        }
                                    });
                        }
                        else
                        {
                            Listador listador = new Listador(UsuarioLogin.getId());
                            cursos = listador.getListadoCursosUsuario();
                        }
                        mostrarItems(cursos);
                        dialog.dismiss();
                    }
                };
                tr.start();
            }
        });

        // Registra callback si se selecciona un item de este AdaptView
        ((ListView) findViewById(R.id.listaCursosJuntos)).setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
        if(UsuarioLogin.isUserOn())
        {
            final Curso cursoSeleccionado = (Curso) listaCursosJuntos.getAdapter().getItem(position);

            Thread tr = new Thread()
            {
                @Override
                public void run()
                {
                    Bundle bundleNombrePersonalizado = new Bundle();
                    bundleNombrePersonalizado.putSerializable("Curso", cursoSeleccionado);

                    Intent intent = new Intent(getApplicationContext(), NombreCursoPersonalizado.class);
                    intent.putExtras(bundleNombrePersonalizado);
                    startActivity(intent);
                }
            };
            tr.start();
        }
        else
        {
            geoLocalizarAula(position);
        }

            }
        });

    }
    public void mostrarItems(ArrayList<Curso> datos)
    {
        adaptadorCursos = new ArrayAdapter<Curso>(this,android.R.layout.simple_list_item_1,datos);
        listaCursosJuntos = (ListView) findViewById(R.id.listaCursosJuntos);
        listaCursosJuntos.setAdapter(adaptadorCursos);
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

    public void onBackPressed()
    {
        Intent start = new Intent(CursoPersonalizado.this,Principal.class);
        startActivity(start);
        finishActivity(0);
    }
}
