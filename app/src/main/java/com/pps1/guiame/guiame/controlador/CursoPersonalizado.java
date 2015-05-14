package com.pps1.guiame.guiame.controlador;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
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
import com.pps1.guiame.guiame.persistencia.dao.Listador;
import com.pps1.guiame.guiame.utils.Utils;

import java.util.ArrayList;

public class CursoPersonalizado extends Activity
{
    private Button btnBuscarCurso;
    private ListView listaCursosJuntos;
    private EditText txtNombreMateria;
    ArrayAdapter<String> adaptador;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_curso_personalizado);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); //Escondemos el teclado
        btnBuscarCurso = (Button)findViewById(R.id.btnBuscarCurso);
        txtNombreMateria = (EditText)findViewById(R.id.txtMateriaNombre);

        btnBuscarCurso.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Thread tr = new Thread()
                {
                    @Override
                    public void run()
                    {
                        Listador listador = new Listador(UsuarioLogin.getUsuario(), txtNombreMateria.getText().toString());
                        final ArrayList<String> cursos = listador.getListadoCursosJuntos();
                        runOnUiThread(
                                new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        mostrarItems(cursos);
                                    }
                                });
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
                final String itemSeleccionado = (String) listaCursosJuntos.getAdapter().getItem(position);

                Thread tr = new Thread()
                {
                    @Override
                    public void run()
                    {
                        String idCurso = Utils.getCursoPersonalizado(itemSeleccionado);

                        if(idCurso == null)
                        {
                            runOnUiThread(
                                    new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getApplicationContext(),
                                                    "No se encontró el curso", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                            return;
                        }

                        Bundle bundleNombrePersonalizado = new Bundle();
                        bundleNombrePersonalizado.putSerializable("idCurso", idCurso);

                        Intent intent = new Intent(getApplicationContext(), NombreCursoPersonalizado.class);
                        intent.putExtras(bundleNombrePersonalizado);
                        startActivity(intent);
                    }
                };
                tr.start();
            }
        });
    }
    public void mostrarItems(ArrayList<String> datos)
    {
        adaptador = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,datos);
        listaCursosJuntos = (ListView) findViewById(R.id.listaCursosJuntos);
        listaCursosJuntos.setAdapter(adaptador);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_curso_personalizado, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onBackPressed()
    {
        Intent start = new Intent(CursoPersonalizado.this,Principal.class);
        startActivity(start);
        finishActivity(0);
    }
}
