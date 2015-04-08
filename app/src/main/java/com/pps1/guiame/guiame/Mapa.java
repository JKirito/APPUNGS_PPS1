package com.pps1.guiame.guiame;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
//TUTORIAL: http://www.androidcurso.com/index.php/tutoriales-android-fundamentos/41-unidad-7-seguridad-y-posicionamiento/223-google-maps-api-v2
public class Mapa extends FragmentActivity implements GoogleMap.OnMapClickListener
{
    private final LatLng UPV = new LatLng(-34.521712, -58.701063);
    private GoogleMap mapa;

    @Override protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);
        mapa = ((SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map)).getMap();
        mapa.setMapType(GoogleMap.MAP_TYPE_SATELLITE); //tipo de mapa, elegido: satelital
        mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(UPV,20)); //se ubica en el mapa segun UPV con zoom de 20. min=2 max=21
        mapa.setMyLocationEnabled(true); //visualizacion de la posicion con un triangulo azul
        mapa.getUiSettings().setZoomControlsEnabled(false); //configurar las acciones del interfaz de usuario
        mapa.getUiSettings().setCompassEnabled(true);
        mapa.addMarker(new MarkerOptions()
                .position(UPV)
                .title("UPV")
                .snippet("UNGS")
                .icon(BitmapDescriptorFactory
                        .fromResource(android.R.drawable.ic_menu_compass)) //iconito
                .anchor(0.5f, 0.5f));
        mapa.setOnMapClickListener(this); //escucha pulsaciones en la pantalla
    }

    public void moveCamera(View view)
    {
        mapa.moveCamera(CameraUpdateFactory.newLatLng(UPV));
    }

    public void animateCamera(View view)
    {
        if (mapa.getMyLocation() != null)
            mapa.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng( mapa.getMyLocation().getLatitude(), mapa.getMyLocation().getLongitude()), 15));
    }

    public void addMarker(View view)
    {
        mapa.addMarker(new MarkerOptions().position(
                new LatLng(mapa.getCameraPosition().target.latitude,
                        mapa.getCameraPosition().target.longitude)));

    }

    @Override
    public void onMapClick(LatLng puntoPulsado)
    {
        mapa.addMarker(new MarkerOptions().position(puntoPulsado).
                icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
    }
}