package com.pps1.guiame.guiame.controlador;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pps1.guiame.guiame.R;
import com.pps1.guiame.guiame.dto.Curso;
import com.pps1.guiame.guiame.persistencia.dao.Ingresador;
import com.pps1.guiame.guiame.persistencia.dao.Listador;
import com.pps1.guiame.guiame.persistencia.dao.Verificador;

import java.util.ArrayList;
import java.util.List;


public class Ingreso extends ActionBarActivity
{

    private EditText txtDni;
    private EditText txtContraseña;
    private Button btnAceptar;
    private Button btnCancelar;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingreso);

        //Obtenemos una referencia a los controles de la interfaz
        txtDni = (EditText)findViewById(R.id.txtDni);
        txtContraseña = (EditText)findViewById(R.id.txtContraseña);
        btnAceptar = (Button)findViewById(R.id.btnAceptar);
        btnCancelar = (Button)findViewById(R.id.btnCancelar);
        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        //Implementamos el evento “click” del botón
        btnAceptar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialog.setMessage("Verificando usuario...");
                dialog.show();

                final String dni = txtDni.getText().toString();
                final String pass = txtContraseña.getText().toString();
                final Ingresador ingresador = new Ingresador(dni, pass);
                final Verificador verificador = new Verificador (dni, pass);
                Thread thread = new Thread(new Runnable(){
                    @Override
                    public void run()
                    {
                        try
                        {

                            String resultados = ingresador.resultadoJSON();
                            final List<String> errores = verificador.validarUsuario(resultados);

                            if(errores.size() > 0)
                            {
                                runOnUiThread(
                                        new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(getApplicationContext(),
                                                        errores.get(0), Toast.LENGTH_SHORT).show();
                                                dialog.dismiss(); //Cierra el dialog
                                            }
                                        });

                                return;
                            }

                            ingresador.guardarDatosUsuario();
                            Listador listador = new Listador(Perfil.getId());
                            final ArrayList<Curso> cursos = listador.getListadoCursosUsuario();
                            if(!cursos.isEmpty())
                            {
                                Bundle bundleMaterias = new Bundle();
                                bundleMaterias.putSerializable("Cursos", cursos);
                                bundleMaterias.putBoolean("AgregarCurso", true);
                                Intent intent = new Intent(getApplicationContext(), ListaCursos.class);
                                intent.putExtras(bundleMaterias);
                                startActivity(intent);
                                dialog.dismiss(); //Cierra el dialog
                            }
                            else
                            {
                                Intent intent = new Intent(getApplicationContext(), CursoPersonalizado.class);
                                startActivity(intent);
                            }
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                });
                thread.start();
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creamos el Intent
                Intent intent = new Intent(Ingreso.this, Principal.class);
                startActivity(intent);
                finish();
            }
        });
    }


    @Override
    public void onBackPressed()
    {
        Intent start = new Intent(Ingreso.this,Principal.class);
        startActivity(start);
        finish();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE ||
                newConfig.orientation == Configuration.ORIENTATION_PORTRAIT)
        {
            setContentView(R.layout.activity_ingreso);
        }
    }
}