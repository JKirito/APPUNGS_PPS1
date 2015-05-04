package com.pps1.guiame.guiame.controlador;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pps1.guiame.guiame.R;
import com.pps1.guiame.guiame.dto.Aula;
import com.pps1.guiame.guiame.persistencia.dao.AulaDAO;


public class AulaAAgregar extends ActionBarActivity
{

    private EditText txtAula;
    private Button btnLocalizar;
    private LocationManager locManager;
    private LocationListener locListener;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_aula);


        btnLocalizar = (Button)findViewById(R.id.btnAgregar);
        txtAula = (EditText)findViewById(R.id.txtAula);

        //Obtenemos el numero de aula y la posicion del administrador. Con eso creamos un Aula
        //y se la pasamos al agregador para que la inserte en la BD.
        btnLocalizar.setOnClickListener(new View.OnClickListener()
        {
           @Override
            public void onClick(View v)
            {

                final String numeroAula = txtAula.getText().toString();
                final Location posicion = getPosicionAdmin();

                if(numeroAula == null || numeroAula.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"Ingrese aula", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(posicion == null)
                {
                    Toast.makeText(getApplicationContext(),"No se pudo obtener las coordenadas. Verifique que tiene activado el GPS", Toast.LENGTH_LONG).show();
                    return;
                }
                final Aula aulaNueva = new Aula(numeroAula,posicion.getLatitude(),posicion.getLongitude());

                Thread thread = new Thread(new Runnable(){
                    @Override
                    public void run()
                    {
                        try
                        {
                            new AulaDAO().registrarAula(aulaNueva);
                                runOnUiThread(
                                        new Runnable()
                                        {
                                            @Override
                                            public void run()
                                            {
                                                Toast.makeText(getApplicationContext(),"Aula agregada", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                            Intent intent = new Intent(AulaAAgregar.this, Principal.class);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_agregar_aula, menu);
        return true;
    }

    //Si se eligió la opcion "Salir", cerramos la sesión actual
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if (id == R.id.Salir)
        {
            SessionManager.logout();
            Toast.makeText(this, "Ha cerrado sesión", Toast.LENGTH_SHORT).show();
            Intent intent =
                    new Intent(AulaAAgregar.this, Principal.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    //Estaría bueno pasarlo a Utils =(
    //Obtenemos el servicio del dispositivo para geolocalizar. Luego obtenemos la ultima posicion
    // guardada que haya obtenido el GPS
    //Si ya sabemos la posicion del usuario, la usamos sino, la conseguimos
    public Location getPosicionAdmin()
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
        };
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
