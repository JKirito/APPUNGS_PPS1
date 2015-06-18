package com.pps1.guiame.guiame.controlador;

import android.app.AlertDialog;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.pps1.guiame.guiame.R;
import com.pps1.guiame.guiame.dto.Aula;
import com.pps1.guiame.guiame.persistencia.dao.AulaDAO;
import com.pps1.guiame.guiame.utils.Aviso;
import com.pps1.guiame.guiame.utils.Configuracion;
import com.pps1.guiame.guiame.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;


public class AulaAAgregar extends ActionBarActivity
{

    private EditText txtNumeroAula;
    private Button btnBuscarAula;
    private LocationManager locManager;
    private LocationListener locListener;
    private Object Settings;
    private ListView listaAulas;
    ArrayAdapter<Aula> adaptadorAulas;
    Aviso aviso;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_aula);
        Configuracion.context = this;

        btnBuscarAula = (Button)findViewById(R.id.btnBuscarAula);
        txtNumeroAula = (EditText)findViewById(R.id.txtAula);
        aviso = new Aviso(this);
        final AlertDialog.Builder builder = new AlertDialog.Builder(AulaAAgregar.this);

        //Obtenemos el numero de aula y la posicion del administrador. Con eso creamos un Aula
        //y se la pasamos al agregador para que actualice la tabla aulas con esa posicion.

        ((ListView) findViewById(R.id.listaAulas)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                final Aula aulaSeleccionada = (Aula) listaAulas.getAdapter().getItem(position);
                //final Location posicion = getPosicionAdmin();
                //aulaSeleccionada.setLatitud(posicion.getLatitude());
                //aulaSeleccionada.setLongitud(posicion.getLongitude());

                Toast.makeText(getApplicationContext(),"Posicionando Aula...", Toast.LENGTH_SHORT).show();

                /*
                //Verifico que el aula esté bien y la ubico en el mapa
                if(aulaSeleccionada == null || aulaSeleccionada.getLatitud() == null || aulaSeleccionada.getLongitud() == null)
                {
                    Log.d("numeroAula", aulaSeleccionada.getNumAula());
                    String msj = aulaSeleccionada == null ? "No se ha podido localizar el aula" : "No se tiene su ubicación :(";
                    Toast.makeText(getApplicationContext(), msj, Toast.LENGTH_SHORT).show();
                    aviso.dismiss();
                    return;
                }
                */

                Bundle bundleBuscAula = new Bundle();
                bundleBuscAula.putSerializable("Aula", aulaSeleccionada);

                Intent intent = new Intent(getApplicationContext(), MapaAula.class);
                intent.putExtras(bundleBuscAula);
                startActivity(intent);
                aviso.dismiss();
            }
        });

        btnBuscarAula.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String numeroAula = txtNumeroAula.getText().toString().trim();
                aviso.setMessage("Buscando...");
                aviso.show();

                Thread tr = new Thread() {
                    @Override
                    public void run() {
                        final ArrayList<Aula> aulas;

                        try {
                            aulas = new AulaDAO().getListadoAulas(numeroAula);
                        } catch (IOException e) {
                            e.printStackTrace();
                            runOnUiThread(
                                    new Runnable() {
                                        @Override
                                        public void run() {
                                            aviso.dismiss();
                                            Utils.verDetallesInfo("Ups!", "Hubo un error al obtener las aulas, intente de nuevo", builder);
                                        }
                                    });
                            return;
                        } catch (Exception e) {
                            e.printStackTrace();
                            final String msjError = e.getMessage();
                            runOnUiThread(
                                    new Runnable() {
                                        @Override
                                        public void run() {
                                            aviso.dismiss();
                                            Utils.verDetallesInfo("Esto es embarazoso", "Hubo un error al obtener las aulas, intente de nuevo", builder);
                                        }
                                    });
                            return;
                        }
                        runOnUiThread(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        if (aulas == null || aulas.isEmpty()) {
                                            Toast.makeText(getApplicationContext(),
                                                    "No se han encontrado aulas. Pruebe escribir otro numero", Toast.LENGTH_LONG).show();
                                            return;
                                        }
                                        mostrarItems(aulas);
                                    }
                                });

                        aviso.dismiss();
                    }
                };
                tr.start();
            }
        });
    }

    //Muestra el numero de  aula en la lista
    public void mostrarItems(ArrayList<Aula> datos)
    {
        adaptadorAulas = new ArrayAdapter<Aula>(this,android.R.layout.simple_list_item_1,datos);
        listaAulas = (ListView) findViewById(R.id.listaAulas);
        listaAulas.setAdapter(adaptadorAulas);
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
            Perfil.logout();
            Toast.makeText(this, "Ha cerrado sesión", Toast.LENGTH_SHORT).show();
            Intent intent =
                    new Intent(AulaAAgregar.this, Principal.class);
            startActivity(intent);
            finish();
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
//        boolean gpsStatus = locManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
//        if (!gpsStatus)
//        {
//            Utils.displayPromptForEnablingGPS(AulaAAgregar.this);
//        }
//        else
//        {
        locManager=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
        locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locListener);
        Location loc= locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (loc == null)
        {
            locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locListener);
            loc=locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        return loc;
//        }
//        return null;
    }

    public void onBackPressed()
    {
        Intent start = new Intent(AulaAAgregar.this,Principal.class);
        startActivity(start);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(adaptadorAulas != null)
            this.startActivity(this.getIntent());
    }
}
