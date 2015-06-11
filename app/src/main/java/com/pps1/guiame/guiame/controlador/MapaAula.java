package com.pps1.guiame.guiame.controlador;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.pps1.guiame.guiame.R;
import com.pps1.guiame.guiame.datos.CoordenadasEdificiosUNGS;
import com.pps1.guiame.guiame.dto.Aula;
import com.pps1.guiame.guiame.persistencia.dao.AulaDAO;
import com.pps1.guiame.guiame.utils.Utils;

public class MapaAula extends FragmentActivity implements GoogleMap.OnMapClickListener
{
    private GoogleMap mapa;
    private Aula aula;
    private LatLng latLngAula;
    private Button btnModulos;
    private boolean verInfoModulos = true;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);

        btnModulos = (Button)findViewById(R.id.btnModulos);

        aula = (Aula) getIntent().getSerializableExtra("Aula");
        getIntent().getExtras().clear();

        mapa = ((SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map)).getMap();
        mapa.setMapType(GoogleMap.MAP_TYPE_SATELLITE); //tipo de mapa, elegido: satelital
        mapa.setMyLocationEnabled(true); //visualizacion de la posicion con un triangulo azul
        mapa.getUiSettings().setZoomControlsEnabled(true); //configurar las acciones del interfaz de usuario
        mapa.getUiSettings().setCompassEnabled(true);
        CameraPosition posicionCamara = new CameraPosition.Builder()
                .target(CoordenadasEdificiosUNGS.UNGS) //Centra el mapa según posicion UNGS
                .zoom(17)                   //zoom min=2 max=21
                .bearing(150) // Orientaciòn de la camara al norte
                .build();    // Crea la nueva posición de la camara
        mapa.animateCamera(CameraUpdateFactory.newCameraPosition(posicionCamara));
       //agregarMarcadoresInformativos();
        mapa.setOnMapClickListener(this); //escucha pulsaciones en la pantalla

        if(aula.getLatitud() != null && aula.getLongitud() != null)
        {
            latLngAula = new LatLng(aula.getLatitud(), aula.getLongitud());
            agregarMarkerAula(latLngAula, aula);
        }


    }

    public void agregarMarcadoresInformativos(View view)
    {
        if(verInfoModulos)
        {
            //Agrego marcador de la UNGS
            mapa.addMarker(new MarkerOptions()
                    .position(CoordenadasEdificiosUNGS.UNGS)
                    .title("UNGS")
                    .snippet("Entrada Principal")
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_ungs)).anchor(0.2f, 0.2f));

            mapa.addMarker(new MarkerOptions()
                    .position(CoordenadasEdificiosUNGS.BIBLIO)
                    .title("Biblioteca (Ubyd)")
                    .icon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

            mapa.addMarker(new MarkerOptions()
                    .position(CoordenadasEdificiosUNGS.ICI)
                    .title("ICI - Módulo 2b")
                    .icon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

            mapa.addMarker(new MarkerOptions()
                    .position(CoordenadasEdificiosUNGS.IDEI)
                    .title("IDEI - Módulo 4")
                    .icon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

            mapa.addMarker(new MarkerOptions()
                    .position(CoordenadasEdificiosUNGS.IDH)
                    .title("IDH - Módulo 5")
                    .icon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

            mapa.addMarker(new MarkerOptions()
                    .position(CoordenadasEdificiosUNGS.ICO)
                    .title("ICO - Módulo 6")
                    .icon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

            mapa.addMarker(new MarkerOptions()
                    .position(CoordenadasEdificiosUNGS.MOD_1)
                    .title("Módulo 1")
                    .icon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

            mapa.addMarker(new MarkerOptions()
                    .position(CoordenadasEdificiosUNGS.MOD_2a)
                    .title("Módulo 2a")
                    .icon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

            mapa.addMarker(new MarkerOptions()
                    .position(CoordenadasEdificiosUNGS.MOD_3)
                    .title("Módulo 3")
                    .icon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

            mapa.addMarker(new MarkerOptions()
                    .position(CoordenadasEdificiosUNGS.MOD_7)
                    .title("Módulo 7")
                    .icon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

            mapa.addMarker(new MarkerOptions()
                    .position(CoordenadasEdificiosUNGS.MOD_9)
                    .title("Módulo 9 - Buffet - Bar")
                    .icon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

            mapa.addMarker(new MarkerOptions()
                    .position(CoordenadasEdificiosUNGS.ANFITEATRO)
                    .title("Anfiteatro")
                    .icon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

            mapa.addMarker(new MarkerOptions()
                    .position(CoordenadasEdificiosUNGS.CAMPO_DEPORTES)
                    .title("Campo de deportes de la UNGS")
                    .icon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

            mapa.addMarker(new MarkerOptions()
                    .position(CoordenadasEdificiosUNGS.ESCUELA_INFANTIL)
                    .title("Jardín")
                    .icon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));


            this.btnModulos.setText(getString(R.string.ocultarModulos));
            this.verInfoModulos = false;
        }
        else
        {
            this.verInfoModulos = true;
            mapa.clear();
            this.btnModulos.setText(getString(R.string.verModulos));
            agregarMarkerAula(latLngAula, aula);
        }

    }


    private void agregarMarkerAula(LatLng latLngAula, Aula aula)
    {
        //Agrego marcador en el aula
        mapa.addMarker(new MarkerOptions().position(latLngAula)
                .title("AULA " + aula.getNumAula()).
                        snippet(Utils.ubicacionAula(aula.getNumAula()))
                .icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_RED)))
                .showInfoWindow();
    }

    public void moveCamera(View view)
    {
        //se ubica en el mapa segun UNGS con zoom de 17. min=2 max=21
        mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(CoordenadasEdificiosUNGS.UNGS, 17));
    }

    @Override
    public void onMapClick(final LatLng puntoPulsado)
    {
        mapa.clear();
        mapa.addMarker(new MarkerOptions().position(puntoPulsado).icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

        Thread thread = new Thread(new Runnable(){
            @Override
            public void run()
            {
                try
                {
                    //new AulaDAO().geolocalizarAula(aulaSeleccionada);
                    aula.setLatitud(puntoPulsado.latitude);
                    aula.setLongitud(puntoPulsado.longitude);
                    new AulaDAO().geolocalizarAula(aula);
                    //Intent intent = new Intent(AulaAAgregar.this, Principal.class);
                    //startActivity(intent);
                    //aviso.dismiss();
                    //finish();
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