package com.pps1.guiame.guiame.controlador;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pps1.guiame.guiame.R;
import com.pps1.guiame.guiame.persistencia.dao.Buscador;
import com.pps1.guiame.guiame.persistencia.dao.Listador;

import java.util.ArrayList;

public class NombreCursoPersonalizado extends Activity
{
    private EditText txtNombreMateria;
    private Button btnGuardarCurso;
    private String idCurso;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nombre_curso_personalizado);
        txtNombreMateria = (EditText)findViewById(R.id.txtNombreMateria);
        btnGuardarCurso = (Button)findViewById(R.id.btnGuardarNombre);
        setTitle("Personalizar nombre de curso");


        btnGuardarCurso.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Thread tr = new Thread()
                {
                    @Override
                    public void run()
                    {
                        idCurso = (String) getIntent().getExtras().get("idCurso");
                        getIntent().getExtras().clear();

                        Buscador buscador = new Buscador(SessionManager.getUser());
                        buscador.registrarCursoPersonalizado(idCurso);
                        runOnUiThread(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(),
                                                "Se agregó el curso correctamente", Toast.LENGTH_SHORT).show();
                                    }
                                });
                        Intent intent = new Intent(getApplicationContext(), Principal.class);
                        startActivity(intent);
                    }
                };
                tr.start();
            }
        });

    }

    //Al presionar el botón Atrás vuelve a la clase CursoPersonalizado
    @Override
    public void onBackPressed()
    {
        Intent start = new Intent(NombreCursoPersonalizado.this,CursoPersonalizado.class);
        startActivity(start);
        finishActivity(0);
    }
}
