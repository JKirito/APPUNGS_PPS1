package com.pps1.guiame.guiame.controlador;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pps1.guiame.guiame.R;
import com.pps1.guiame.guiame.persistencia.dao.Modificador;
import com.pps1.guiame.guiame.persistencia.dao.Registrador;
import com.pps1.guiame.guiame.persistencia.dao.Verificador;

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
    ProgressDialog dialog;

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

        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        if(Perfil.isUserOn())
        {
            setTitle("Modificacion de perfil");
            nombreApellido.setText(Perfil.getNombre());
            dni.setText(Perfil.getUsuario());
            mail.setText(Perfil.getMail());
            pass.setText(Perfil.getPassword());
        }

        btnCancelar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //Creamos el Intent
                Intent intent =
                        new Intent(Registro.this, Principal.class);
                startActivity(intent);
                finish();
            }
        });

        btnAceptar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final Verificador verificador = new Verificador(nombreApellido.getText().toString(),dni.getText().toString(), mail.getText().toString(), pass.getText().toString(), pass2.getText().toString());
                if(Perfil.isUserOn())
                {
                    final Modificador modificador = new Modificador(nombreApellido.getText().toString(),dni.getText().toString(), mail.getText().toString(), pass.getText().toString(), pass2.getText().toString());
                    dialog.setMessage("Guardando cambios...");
                    dialog.show();

                    Thread thread = new Thread(new Runnable(){
                        @Override
                        public void run()
                        {
                            try
                            {
                                final List<String> errores = verificador.validarDatosRegistro();

                                if(errores.size() > 0)
                                {
                                    StringBuilder todosErrores = new StringBuilder();
                                    for(String e : errores)
                                    {
                                        todosErrores.append(e+"\n");
                                    }
                                    final String errors = todosErrores.toString();
                                    runOnUiThread(
                                            new Runnable() {
                                                @Override
                                                public void run()
                                                {
                                                    Toast.makeText(getApplicationContext(),
                                                            errors, Toast.LENGTH_SHORT).show();
                                                    dialog.dismiss(); //Cierra el dialog
                                                }
                                            });

                                    return;
                                }

                                modificador.actualizarDatos();
                                Perfil.logout();
                                Intent intent = new Intent(Registro.this, Ingreso.class);
                                startActivity(intent);
                                runOnUiThread(
                                        new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(getApplicationContext(), "Se actualizaron los datos correctamente. Por favor ingrese de nuevo.", Toast.LENGTH_LONG).show();
                                                dialog.dismiss(); //Cierra el dialog
                                            }
                                        });
                                finish();
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }
                    });
                    thread.start();
                }
                else
                {
                    final Registrador registrador = new Registrador(nombreApellido.getText().toString(), dni.getText().toString(), mail.getText().toString(), pass.getText().toString(), pass2.getText().toString());
                    dialog.setMessage("Registrando usuario...");
                    dialog.show();
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                final List<String> errores = verificador.validarDatosRegistro();

                                if (errores.size() > 0)
                                {
                                    StringBuilder todosErrores = new StringBuilder();
                                    for (String e : errores)
                                    {
                                        todosErrores.append(e + "\n");
                                    }
                                    final String errors = todosErrores.toString();
                                    runOnUiThread(
                                            new Runnable()
                                            {
                                                @Override
                                                public void run()
                                                {
                                                    Toast.makeText(getApplicationContext(),
                                                            errors, Toast.LENGTH_SHORT).show();
                                                    dialog.dismiss(); //Cierra el dialog
                                                }
                                            });

                                    return;
                                }

                                registrador.registrarDatos();
                                Intent intent = new Intent(Registro.this, Ingreso.class);
                                startActivity(intent);
                                runOnUiThread(
                                        new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(getApplicationContext(), "Se registró correctamente! :)", Toast.LENGTH_LONG).show();
                                                dialog.dismiss(); //Cierra el dialog
                                            }
                                        });
                                finish();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    thread.start();
                }

            }

        });
    }

   /* @Override
    public void onResume()
    {
        super.onResume();

        nombreApellido.setText("");
        dni.setText("");
        mail.setText("");
        pass.setText("");
        pass2.setText("");
    }*/

   @Override
   public void onBackPressed()
   {
       Intent start = new Intent(Registro.this,Principal.class);
       startActivity(start);
       finish();
   }
}
