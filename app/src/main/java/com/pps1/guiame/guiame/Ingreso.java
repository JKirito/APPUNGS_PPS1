package com.pps1.guiame.guiame;

/**
 * Created by Agustina on 25/03/2015.
 */

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.security.Timestamp;
import java.util.ArrayList;
import java.util.Date;
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
                Thread thread = new Thread(new Runnable(){
                    @Override
                    public void run()
                    {
                        try
                        {
                            final List<String> errores = ingresador.ingresarUsuario();

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

                            // Guardo el user y pass
                            SessionManager.setUser(dni);
                            SessionManager.setPassword(pass);
                            SessionManager.setInitSession(new Date());

                            Listador listador = new Listador(txtDni.getText().toString());
                            final ArrayList<String> materias = listador.getListadoMateriasUsuario();
                            Bundle bundleMaterias = new Bundle();
                            bundleMaterias.putSerializable("Materias", materias);
                            Intent intent = new Intent(getApplicationContext(), ListaMaterias.class);
                            intent.putExtras(bundleMaterias);
                            startActivity(intent);
                            dialog.dismiss(); //Cierra el dialog
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

        btnCancelar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //Creamos el Intent
                Intent intent = new Intent(Ingreso.this, Principal.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume()
   {
       super.onResume();

       txtDni.setText("");
       txtContraseña.setText("");
    }




}