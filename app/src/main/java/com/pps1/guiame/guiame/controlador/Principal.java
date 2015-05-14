package com.pps1.guiame.guiame.controlador;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.pps1.guiame.guiame.R;
import com.pps1.guiame.guiame.persistencia.dao.Listador;

import java.util.ArrayList;

public class Principal extends Activity
{
    ProgressDialog dialog;
    private Button btnRegistrarse;
    private Button btnIngresar;
    private Button btnVistaRapida;
    private Button btnAgregarAula;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_principal);
        btnRegistrarse = (Button)findViewById(R.id.btnRegistrarse);
        btnIngresar = (Button)findViewById(R.id.btnIngresar);
        btnVistaRapida = (Button)findViewById(R.id.btnVistaRapida);
        btnAgregarAula = (Button)findViewById(R.id.btnAgregarAula);
        dialog = new ProgressDialog(this);
        habilitarBotones();

        btnRegistrarse.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //Creamos el Intent
                Intent intent =
                        new Intent(Principal.this, Registro.class);
                startActivity(intent);
                finish(); //Agregamos esto para hacer que se cierre este activity al crear otro,
                          // sino quedan solapados

            }
        });

        btnIngresar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.d("USER", UsuarioLogin.getUsuario() + "");
                Log.d("PASSW", UsuarioLogin.getPassword() + "");
                // si NO está logeado, que vaya a la pantalla de ingreso
                if(UsuarioLogin.getUsuario() == null || UsuarioLogin.getUsuario().isEmpty() ||
                        UsuarioLogin.getPassword() == null || UsuarioLogin.getPassword().isEmpty())
                {
                    Intent intent = new Intent(Principal.this, Ingreso.class);
                    startActivity(intent);
                    finish();
                }
                // Si está logeado lo mando a la pantalla con las materias
                else
                {
                    dialog.setMessage("Cargando...");
                    dialog.show();
                    Thread tr = new Thread()
                    {
                        @Override
                        public void run(){
                            Listador listador = new Listador(UsuarioLogin.getUsuario());
                            final ArrayList<String> materias = listador.getListadoCursosUsuario();
                            Bundle bundleMaterias = new Bundle();
                            bundleMaterias.putSerializable("Materias", materias);
                            Intent intent = new Intent(getApplicationContext(), ListaCursos.class);
                            intent.putExtras(bundleMaterias);
                            startActivity(intent);
                            dialog.dismiss(); //Cierra el dialog
                            finish();
                        }
                    };
                    tr.start();
                }
            }
        });

        btnVistaRapida.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialog.setMessage("Actualizando datos...");
                dialog.show();
                Thread tr = new Thread()
                {
                    @Override
                    public void run()
                    {
                        Listador listador = new Listador();
                        ArrayList<String> materias = listador.getListadoCursosDisponibles();
                        Bundle bundleMaterias = new Bundle();
                        bundleMaterias.putSerializable("Materias", materias);
                        //Creamos el Intent
                        Intent intent = new Intent(getApplicationContext(), ListaCursos.class);
                        intent.putExtras(bundleMaterias);
                        startActivity(intent);
                        dialog.dismiss(); //Cierra el dialog
                        finish();
                    }
                };
                tr.start();
            }
        });
        btnAgregarAula.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread tr = new Thread() {
                    @Override
                    public void run() {
                        //Creamos el Intent
                        Intent intent = new Intent(getApplicationContext(), AulaAAgregar.class);
                        startActivity(intent);
                        finish();
                    }
                };
                tr.start();
            }
        });
    }

    /*Si el usuario esta conectado y en Principal, sacamos el boton de Registrarse, si es Admin
    ponemos el boton de AgregarAula
     */
    private void habilitarBotones()
    {
        if(UsuarioLogin.isUserOn())
        {
            btnRegistrarse.setVisibility(View.INVISIBLE);
            btnIngresar.setText(R.string.misCursos);
            if(UsuarioLogin.isAdministrador())
            {
                btnAgregarAula.setVisibility(View.VISIBLE);
            }
            else
            {
                btnAgregarAula.setVisibility(View.INVISIBLE);
            }
        }
        else
        {
            btnRegistrarse.setVisibility(View.VISIBLE);
            btnIngresar.setText(R.string.ingresar);
        }
    }

   /* @Override
    protected void onResume()
    {
        super.onResume();
    }*/


    public void onBackPressed()
    {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}