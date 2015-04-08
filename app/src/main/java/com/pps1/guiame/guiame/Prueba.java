package com.pps1.guiame.guiame;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;


public class Prueba extends ActionBarActivity
{
    String elemento;
    //TextView elementoAMostrar;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prueba);
        elemento = getIntent().getExtras().getString("elementoSeleccionado");
        //elementoAMostrar.findViewById(R.id.txtPrueba);
        TextView elementoAMostrar = (TextView) findViewById(R.id.txtPrueba);
        elementoAMostrar.setText(elemento);
    }
}
