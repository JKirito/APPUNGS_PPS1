package com.pps1.guiame.guiame.controlador;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pps1.guiame.guiame.R;
import com.pps1.guiame.guiame.persistencia.dao.Enviador;
import com.pps1.guiame.guiame.utils.Aviso;
import com.pps1.guiame.guiame.utils.Utils;
import com.pps1.guiame.guiame.utils.ValidadorMail;

public class Recuperacion extends Activity
{
    private EditText txtMailContrasena;
    private Button btnRecuperar;
    private Button btnCancelarEnvio;
    Aviso aviso;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperacion);
        txtMailContrasena = (EditText)findViewById(R.id.txtMailContrasena);
        btnRecuperar = (Button)findViewById(R.id.btnRecuperar);
        btnCancelarEnvio = (Button)findViewById(R.id.btnCancelarEnvio);
        aviso = new Aviso(this);
        final AlertDialog.Builder builder = new AlertDialog.Builder(Recuperacion.this);


        btnRecuperar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String mailUsuario = txtMailContrasena.getText().toString();
                final Enviador enviador = new Enviador(mailUsuario);
                aviso.setMessage("Recuperando contraseña...");
                aviso.show();

                boolean mailValido = ValidadorMail.validarEmail(mailUsuario);

                if(!mailValido)
                {
                    aviso.dismiss();
                    Toast.makeText(getApplicationContext(), "Mail inválido", Toast.LENGTH_SHORT).show();
                    return;
                }

                Thread tr = new Thread() {
                    @Override
                    public void run() {
                        try
                        {
                            if(!enviador.isMailExistente())
                            {
                                runOnUiThread(
                                        new Runnable() {
                                            @Override
                                            public void run() {
                                                aviso.dismiss();
                                                Utils.verDetallesInfo("Recorcholis!", "El mail ingresado no existe!", builder);
                                            }
                                        });

                                return;
                            }
                            enviador.enviarMailRecuperarContraseña();
                            aviso.dismiss();
                            runOnUiThread(
                                new Runnable() {
                                        @Override
                                        public void run() {
                                    Toast.makeText(getApplicationContext(), "Se ha enviado un mail a su casilla de correo con la nueva contraseña", Toast.LENGTH_LONG).show();
                                    irAIngreso();
                                }
                                                              });

                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                            final String msjError = e.getMessage();
                            runOnUiThread(
                                    new Runnable() {
                                        @Override
                                        public void run() {
                                            aviso.dismiss();
                                            Utils.verDetallesInfo("Error!", msjError, builder);
                                        }
                                    });
                            return;
                        }
                    }
                };
                tr.start();

            }
        });

        btnCancelarEnvio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                irAIngreso();
            }
        });
    }

    public void irAIngreso(){
        //Creamos el Intent
        Intent intent = new Intent(Recuperacion.this, Ingreso.class);
        startActivity(intent);
        finish();
    }

    public void onBackPressed()
    {
        irAIngreso();
    }


}
