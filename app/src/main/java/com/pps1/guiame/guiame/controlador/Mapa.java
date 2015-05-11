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
import com.pps1.guiame.guiame.datos.Coordenadas;
import com.pps1.guiame.guiame.dto.Aula;
import com.pps1.guiame.guiame.utils.Utils;

public class Mapa extends FragmentActivity implements GoogleMap.OnMapClickListener
{
    private GoogleMap mapa;
    private int cantPulsaciones;
    private Aula aula;
    private LatLng latLngAula;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);

        aula = (Aula) getIntent().getSerializableExtra("Aula");
        getIntent().getExtras().clear();

        // Si tengo la coordenada del aula, muestro
        latLngAula = aula != null ? new LatLng(aula.getLatitud(), aula.getLongitud()) : Coordenadas.UNGS;

        mapa = ((SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map)).getMap();
        mapa.setMapType(GoogleMap.MAP_TYPE_SATELLITE); //tipo de mapa, elegido: satelital
        mapa.setMyLocationEnabled(true); //visualizacion de la posicion con un triangulo azul
        mapa.getUiSettings().setZoomControlsEnabled(true); //configurar las acciones del interfaz de usuario
        mapa.getUiSettings().setCompassEnabled(true);
        CameraPosition posicionCamara = new CameraPosition.Builder()
                .target(Coordenadas.UNGS) //Centra el mapa según posicion UNGS
                .zoom(17)                   //zoom min=2 max=21
                .bearing(150) // Orientaciòn de la camara al norte
                .build();    // Crea la nueva posición de la camara
        mapa.animateCamera(CameraUpdateFactory.newCameraPosition(posicionCamara));
       //agregarMarcadoresInformativos();
        mapa.setOnMapClickListener(this); //escucha pulsaciones en la pantalla
        agregarMarkerAula(latLngAula, aula);


    }

    public void agregarMarcadoresInformativos(View view)
    {
        cantPulsaciones++;
        if(cantPulsaciones <=1 )
        {
            //Agrego marcador de la UNGS
            mapa.addMarker(new MarkerOptions()
                    .position(Coordenadas.UNGS)
                    .title("UNGS")
                    .snippet("Entrada Principal")
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_ungs)).anchor(0.5f, 0.5f));

            mapa.addMarker(new MarkerOptions()
                    .position(Coordenadas.BIBLIO)
                    .title("Biblioteca (Ubyd)")
                    .icon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

            mapa.addMarker(new MarkerOptions()
                    .position(Coordenadas.ICI)
                    .title("ICI - Módulo 2b")
                    .icon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

            mapa.addMarker(new MarkerOptions()
                    .position(Coordenadas.IDEI)
                    .title("IDEI - Módulo 4")
                    .icon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

            mapa.addMarker(new MarkerOptions()
                    .position(Coordenadas.IDH)
                    .title("IDH - Módulo 5")
                    .icon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

            mapa.addMarker(new MarkerOptions()
                    .position(Coordenadas.ICO)
                    .title("ICO - Módulo 6")
                    .icon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

            mapa.addMarker(new MarkerOptions()
                    .position(Coordenadas.MOD_1)
                    .title("Módulo 1")
                    .icon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

            mapa.addMarker(new MarkerOptions()
                    .position(Coordenadas.MOD_2a)
                    .title("Módulo 2a")
                    .icon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

            mapa.addMarker(new MarkerOptions()
                    .position(Coordenadas.MOD_3)
                    .title("Módulo 3")
                    .icon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

            mapa.addMarker(new MarkerOptions()
                    .position(Coordenadas.MOD_7)
                    .title("Módulo 7")
                    .icon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

            mapa.addMarker(new MarkerOptions()
                    .position(Coordenadas.MOD_9)
                    .title("Módulo 9 - Buffet - Bar")
                    .icon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        }
        else
        {
            cantPulsaciones = 0;
            mapa.clear();
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
        mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(Coordenadas.UNGS, 17));
    }

    @Override
    public void onMapClick(LatLng puntoPulsado)
    {
        //mapa.addMarker(new MarkerOptions().position(puntoPulsado).icon(BitmapDescriptorFactory
        //.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
    }
}