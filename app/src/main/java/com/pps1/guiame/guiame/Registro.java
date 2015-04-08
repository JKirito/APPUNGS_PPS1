package com.pps1.guiame.guiame;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

public class Registro extends ActionBarActivity
{
    private Button btnAceptar;
    private Button btnCancelar;
    private EditText nombreApellido;
    private EditText dni;
    private EditText mail;
    private EditText pass;
    private EditText pass2;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        nombreApellido = (EditText) findViewById(R.id.txtNombre);
        dni = (EditText) findViewById(R.id.txtDni);
        mail = (EditText) findViewById(R.id.txtMail);
        pass = (EditText) findViewById(R.id.txtContraseña);
        pass2 = (EditText) findViewById(R.id.txtRepetirContraseña);

        btnAceptar = (Button)findViewById(R.id.btnAceptar);
        btnCancelar = (Button)findViewById(R.id.btnCancelar);

        btnCancelar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //Creamos el Intent
                Intent intent =
                        new Intent(Registro.this, Principal.class);
                startActivity(intent);
            }
        });

        btnAceptar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final Registrador registrador = new Registrador(nombreApellido.getText().toString(),dni.getText().toString(), mail.getText().toString(), pass.getText().toString(), pass2.getText().toString());
                Thread thread = new Thread(new Runnable(){
                    @Override
                    public void run()
                    {
                        try
                        {
                            final List<String> errores = registrador.validarDatos();

                            if(errores.size() > 0)
                            {
                                final StringBuilder todosErrores = new StringBuilder();
                                for(String e : errores){
                                    todosErrores.append(e+"\n");
                                }
                                runOnUiThread(
                                        new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(getApplicationContext(),
                                                        todosErrores.toString(), Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                return;
                            }

                            registrador.registrarDatos();

                            Intent intent = new Intent(Registro.this, Ingreso.class);
                            startActivity(intent);
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
    }
}
