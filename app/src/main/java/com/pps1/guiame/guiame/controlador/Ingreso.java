package com.pps1.guiame.guiame.controlador;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.pps1.guiame.guiame.R;
import com.pps1.guiame.guiame.dto.Curso;
import com.pps1.guiame.guiame.persistencia.dao.CursoDAO;
import com.pps1.guiame.guiame.persistencia.dao.Ingresador;
import com.pps1.guiame.guiame.persistencia.dao.Verificador;
import com.pps1.guiame.guiame.utils.Aviso;
import com.pps1.guiame.guiame.utils.Configuracion;

import java.util.ArrayList;
import java.util.List;


public class Ingreso extends ActionBarActivity
{

    private EditText txtDni;
    private EditText txtContraseña;
    private TextView lblOlvideContraseña;
    private Button btnAceptar;
    private Button btnCancelar;
    Aviso aviso;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingreso);
        Configuracion.context = this;

        //Obtenemos una referencia a los controles de la interfaz
        txtDni = (EditText)findViewById(R.id.txtDni);
        txtContraseña = (EditText)findViewById(R.id.txtContraseña);
        lblOlvideContraseña = (TextView)findViewById(R.id.lblOlvideContrasena);
        btnAceptar = (Button)findViewById(R.id.btnAceptar);
        btnCancelar = (Button)findViewById(R.id.btnCancelar);
        aviso = new Aviso(this);
        aviso.setCancelable(false);
        aviso.setCanceledOnTouchOutside(false);

        //Implementamos el evento “click” del botón
        btnAceptar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                aviso.setMessage("Verificando usuario...");
                aviso.show();

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
                            String datosUsurioJSON = ingresador.resultadoJSON();
                            final List<String> errores = verificador.validarUsuario(datosUsurioJSON);

                            if(errores.size() > 0)
                            {
                                runOnUiThread(
                                        new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(getApplicationContext(),
                                                        errores.get(0), Toast.LENGTH_SHORT).show();
                                                aviso.dismiss(); //Cierra el aviso
                                            }
                                        });

                                return;
                            }

                            ingresador.guardarDatosUsuario(datosUsurioJSON);
                            final ArrayList<Curso> cursos = new CursoDAO().getListadoCursosUsuario(Perfil.getId());
                            if(!cursos.isEmpty())
                            {
                                Bundle bundleMaterias = new Bundle();
                                bundleMaterias.putSerializable("Cursos", cursos);
                                Intent intent = new Intent(getApplicationContext(), ListaCursos.class);
                                intent.putExtras(bundleMaterias);
                                startActivity(intent);
                                aviso.dismiss(); //Cierra el aviso
                            }
                            else
                            {
                                Bundle bundleCurso = new Bundle();
                                bundleCurso.putBoolean("AgregarCurso", true);
                                Intent intent = new Intent(getApplicationContext(), CursoPersonalizado.class);
                                intent.putExtras(bundleCurso);
                                startActivity(intent);
                            }
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                            final String msjError = e.getMessage();
                            runOnUiThread(
                                    new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getApplicationContext(),
                                                    msjError, Toast.LENGTH_SHORT).show();
                                        }
                                    });
                            aviso.dismiss();
                            return;
                        }
                    }
                });
                thread.start();
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //Creamos el Intent
                Intent intent = new Intent(Ingreso.this, Principal.class);
                startActivity(intent);
                finish();
            }
        });

        lblOlvideContraseña.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //Creamos el Intent
                Intent intent = new Intent(Ingreso.this, Recuperacion.class);
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
}

