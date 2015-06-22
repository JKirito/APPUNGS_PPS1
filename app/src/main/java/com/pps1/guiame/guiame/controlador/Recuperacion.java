package com.pps1.guiame.guiame.controlador;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.pps1.guiame.guiame.R;
import com.pps1.guiame.guiame.persistencia.dao.Enviador;

public class Recuperacion extends Activity
{
    private EditText txtMailContrasena;
    private Button btnRecuperar;
    private Button btnCancelarEnvio;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperacion);
        txtMailContrasena = (EditText)findViewById(R.id.txtMailContrasena);
        btnRecuperar = (Button)findViewById(R.id.btnRecuperar);
        btnCancelarEnvio = (Button)findViewById(R.id.btnCancelarEnvio);


        btnRecuperar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Enviador enviador = new Enviador(txtMailContrasena.getText().toString());
                try
                {
                    enviador.enviarMailRecuperarContraseña();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

        btnCancelarEnvio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creamos el Intent
                Intent intent = new Intent(Recuperacion.this, Principal.class);
                startActivity(intent);
                finish();
            }
        });
    }


}
