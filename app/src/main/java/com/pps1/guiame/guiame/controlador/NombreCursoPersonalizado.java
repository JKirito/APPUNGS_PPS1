package com.pps1.guiame.guiame.controlador;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pps1.guiame.guiame.R;
import com.pps1.guiame.guiame.dto.Curso;
import com.pps1.guiame.guiame.persistencia.dao.CursoDAO;

public class NombreCursoPersonalizado extends Activity
{
    private EditText txtNombreMateria;
    private Button btnGuardarCurso;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nombre_curso_personalizado);
        txtNombreMateria = (EditText)findViewById(R.id.txtNombreMateria);
        btnGuardarCurso = (Button)findViewById(R.id.btnGuardarNombre);
        final Curso curso = (Curso) getIntent().getExtras().get("Curso");
        getIntent().getExtras().clear();
        txtNombreMateria.setText(curso.getNombre());
        setTitle("Personalizar nombre de curso");

        btnGuardarCurso.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                curso.setNombre(txtNombreMateria.getText().toString());
                Thread tr = new Thread()
                {
                    @Override
                    public void run()
                    {
                        new CursoDAO().registrarCursoPersonalizado(curso, UsuarioLogin.getId());
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
