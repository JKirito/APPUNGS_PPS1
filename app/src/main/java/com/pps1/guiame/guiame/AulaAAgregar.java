package com.pps1.guiame.guiame;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pps1.guiame.guiame.entities.Aula;

import java.util.HashMap;
import java.util.Map;


public class AulaAAgregar extends ActionBarActivity
{
    private LocationManager locManager;
    private LocationListener locListener;

    private EditText txtAula;
    private Button btnLocalizar;
    private final String PHP_NAME_REGISTRADOR_AULA = "registrarAula.php";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_localizar);


        btnLocalizar = (Button)findViewById(R.id.btnLocalizar);
        txtAula = (EditText)findViewById(R.id.txtAula);

        btnLocalizar.setOnClickListener(new View.OnClickListener()
        {
           @Override
            public void onClick(View v)
            {
                final String numeroAula = txtAula.getText().toString();
                final Location ubicacion = posicionAula();
                final Aula aulaNueva = new Aula(numeroAula,ubicacion.getLatitude(),ubicacion.getLongitude());
                Thread thread = new Thread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        try
                        {
                            registrarAula(aulaNueva);
                            Toast.makeText(getApplicationContext(),
                                    "Aula agregada", Toast.LENGTH_SHORT).show();
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                });
                thread.start();

                Intent intent = new Intent(AulaAAgregar.this, Principal.class);
                startActivity(intent);
            }
        });

    }

    public void registrarAula(Aula aula)
    {
        //La key del map deben ser los nombres de los campos en la tabla
        Map<String, String> datos = new HashMap<String, String>();
        datos.put("numero",aula.getNumAula());
        datos.put("ubicacion",aula.getLatitud()+","+aula.getLongitud());

        String result = Utils.enviarPost(datos, PHP_NAME_REGISTRADOR_AULA);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_localizar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if (id == R.id.Logout)
        {
            SessionManager.logout();
            Toast.makeText(this, "Usuario Deslogeado", Toast.LENGTH_SHORT).show();
            Intent intent =
                    new Intent(AulaAAgregar.this, Principal.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }


    private Location posicionAula()
    {
        locListener = new LocationListener()
        {
            public void onLocationChanged(Location location) { }

            public void onProviderDisabled(String provider) {}

            public void onProviderEnabled(String provider) {}

            public void onStatusChanged(String provider, int status, Bundle extras)
            {
                Log.i("", "Provider Status: " + status);
            }
        };;
        locManager=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
        locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locListener);
        Location loc=locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (loc == null)
        {
            locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0,locListener);
            loc=locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        return loc;
    }
}
