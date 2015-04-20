package com.pps1.guiame.guiame;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.pps1.guiame.guiame.entities.Aula;


public class Prueba extends ActionBarActivity
{
    String elemento;
    //TextView elementoAMostrar;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prueba);
        Aula aula = (Aula) getIntent().getSerializableExtra("Aula");
       // elemento = getIntent().getExtras().getString("Aula");
        //elementoAMostrar.findViewById(R.id.txtPrueba);
        TextView elementoAMostrar = (TextView) findViewById(R.id.txtPrueba);

        elementoAMostrar.setText("AULA "+aula.getNumAula() + "\n" + Utils.ubicacionAula(aula.getNumAula()));
    }
}
