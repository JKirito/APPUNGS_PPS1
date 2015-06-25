package com.pps1.guiame.guiame.controlador;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pps1.guiame.guiame.R;
import com.pps1.guiame.guiame.persistencia.dao.Enviador;
import com.pps1.guiame.guiame.persistencia.dao.Modificador;
import com.pps1.guiame.guiame.persistencia.dao.Registrador;
import com.pps1.guiame.guiame.persistencia.dao.Verificador;
import com.pps1.guiame.guiame.utils.Aviso;
import com.pps1.guiame.guiame.utils.Configuracion;

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
    Aviso aviso;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        Configuracion.context = this;

        nombreApellido = (EditText) findViewById(R.id.txtNombre);
        dni = (EditText) findViewById(R.id.txtDni);
        mail = (EditText) findViewById(R.id.txtMail);
        pass = (EditText) findViewById(R.id.txtContraseña);
        pass2 = (EditText) findViewById(R.id.txtRepetirContraseña);

        btnAceptar = (Button)findViewById(R.id.btnAceptar);
        btnCancelar = (Button)findViewById(R.id.btnCancelar);

        aviso = new Aviso(this);
        aviso.setCancelable(false);
        aviso.setCanceledOnTouchOutside(false);

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
                    aviso.setMessage("Guardando cambios...");
                    aviso.show();

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
                                                    aviso.dismiss(); //Cierra el aviso
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
                                                aviso.dismiss(); //Cierra el aviso
                                            }
                                        });
                                finish();
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
                else
                {
                    final Registrador registrador = new Registrador(nombreApellido.getText().toString(), dni.getText().toString(), mail.getText().toString(), pass.getText().toString(), pass2.getText().toString());
                    aviso.setMessage("Registrando usuario...");
                    aviso.show();
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
                                                    aviso.dismiss(); //Cierra el aviso
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
                                                aviso.dismiss(); //Cierra el aviso
                                            }
                                        });
                                finish();
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
                            Enviador enviador = new Enviador(nombreApellido.getText().toString(),mail.getText().toString());
                            try
                            {
                                enviador.enviarMailBienvenida();
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }
                    });
                    thread.start();
                }

            }

        });
    }

   @Override
   public void onBackPressed()
   {
       Intent start = new Intent(Registro.this,Principal.class);
       startActivity(start);
       finish();
   }
}
