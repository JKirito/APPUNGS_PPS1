package com.pps1.guiame.guiame.controlador;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.pps1.guiame.guiame.R;
import com.pps1.guiame.guiame.dto.Curso;
import com.pps1.guiame.guiame.persistencia.dao.Listador;

import java.io.IOException;
import java.util.ArrayList;

public class Principal extends Activity
{
    ProgressDialog dialog;
    private Button btnRegistrarse;
    private Button btnIngresar;
    private Button btnBuscarCurso;
    private Button btnAgregarAula;
    private Button btnSalir;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_principal);
        btnRegistrarse = (Button)findViewById(R.id.btnRegistrarse);
        btnIngresar = (Button)findViewById(R.id.btnIngresar);
        btnBuscarCurso = (Button)findViewById(R.id.btnBuscar);
        btnAgregarAula = (Button)findViewById(R.id.btnAgregarAula);
        btnSalir = (Button) findViewById((R.id.btnSalir));

        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
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
                Log.d("USER", Perfil.getUsuario() + "");
                Log.d("PASSW", Perfil.getPassword() + "");
                // si NO está logeado, que vaya a la pantalla de ingreso
                if(Perfil.getUsuario() == null || Perfil.getUsuario().isEmpty() ||
                        Perfil.getPassword() == null || Perfil.getPassword().isEmpty())
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
                            Listador listador = new Listador(Perfil.getId());
                            final ArrayList<Curso> cursos;
                            try {
                                cursos = listador.getListadoCursosUsuario();
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
                            Bundle bundleMaterias = new Bundle();
                            bundleMaterias.putSerializable("Cursos", cursos);
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


        btnBuscarCurso.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getApplicationContext(), CursoPersonalizado.class);
                startActivity(intent);
                finish();
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
        btnSalir.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Perfil.logout();
                Toast.makeText(getApplicationContext(),
                        "Ha cerrado sesión", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Principal.this, Principal.class);
                startActivity(intent);
            }
        });
    }

    /*Si el usuario esta conectado y en Principal, sacamos el boton de Registrarse, si es Admin
    ponemos el boton de AgregarAula
     */
    private void habilitarBotones()
    {
        if(Perfil.isUserOn())
        {
            btnRegistrarse.setVisibility(View.INVISIBLE);
            btnIngresar.setText(R.string.misCursos);
            btnSalir.setVisibility(View.VISIBLE);

            if(Perfil.isAdministrador())
            {
                btnAgregarAula.setVisibility(View.VISIBLE);
            }
            else
            {
                btnAgregarAula.setVisibility(View.GONE);
            }
        }
        else
        {
            btnRegistrarse.setVisibility(View.VISIBLE);
            btnIngresar.setText(R.string.ingresar);
        }
    }

    //Para volver atrás!
    public void onBackPressed()
    {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}