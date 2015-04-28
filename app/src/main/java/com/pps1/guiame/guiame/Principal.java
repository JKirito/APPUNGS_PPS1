package com.pps1.guiame.guiame;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class Principal extends Activity
{
    ProgressDialog dialog;
    private Button btnRegistrarse;
    private Button btnIngresar;
    private Button btnVistaRapida;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_principal);
        btnRegistrarse = (Button)findViewById(R.id.btnRegistrarse);
        btnIngresar = (Button)findViewById(R.id.btnIngresar);
        btnVistaRapida = (Button)findViewById(R.id.btnVistaRapida );
        dialog = new ProgressDialog(this);

        btnRegistrarse.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //Creamos el Intent
                Intent intent =
                        new Intent(Principal.this, Registro.class);
                startActivity(intent);
            }
        });

        btnIngresar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.d("USER", SessionManager.getUser() + "");
                Log.d("PASSW", SessionManager.getPassword() + "");
                // si NO está logeado, que vaya a la pantalla de ingreso
                if(SessionManager.getUser() == null || SessionManager.getUser().isEmpty() ||
                        SessionManager.getPassword() == null || SessionManager.getPassword().isEmpty())
                {
                    Intent intent = new Intent(Principal.this, Ingreso.class);
                    startActivity(intent);
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
                            Listador listador = new Listador(SessionManager.getUser());
                            final ArrayList<String> materias = listador.getListadoMateriasUsuario();
                            Bundle bundleMaterias = new Bundle();
                            bundleMaterias.putSerializable("Materias", materias);
                            Intent intent = new Intent(getApplicationContext(), ListaMaterias.class);
                            intent.putExtras(bundleMaterias);
                            startActivity(intent);
                            dialog.dismiss(); //Cierra el dialog
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
                        ArrayList<String> materias = listador.getListadoMaterias();
                        Bundle bundleMaterias = new Bundle();
                        bundleMaterias.putSerializable("Materias", materias);
                        //Creamos el Intent
                        Intent intent = new Intent(getApplicationContext(), ListaMaterias.class);
                        intent.putExtras(bundleMaterias);
                        startActivity(intent);
                        dialog.dismiss(); //Cierra el dialog
                    }
                };
                tr.start();
            }
        });

    }
}