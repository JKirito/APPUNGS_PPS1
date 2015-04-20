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
import com.pps1.guiame.guiame.entities.Aula;

//TUTORIAL: http://www.androidcurso.com/index.php/tutoriales-android-fundamentos/41-unidad-7-seguridad-y-posicionamiento/223-google-maps-api-v2
public class Mapa extends FragmentActivity implements GoogleMap.OnMapClickListener
{
    private final LatLng UNGS = new LatLng(-34.521712, -58.701063);
    private GoogleMap mapa;

    @Override protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);

        Aula aula = (Aula) getIntent().getSerializableExtra("Aula");
        getIntent().getExtras().clear();

        // Si tengo la coordenada del aula, muestro
        LatLng latLngAula = aula != null ? new LatLng(aula.getLatitud(), aula.getLongitud()) : UNGS;

        mapa = ((SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map)).getMap();
        mapa.setMapType(GoogleMap.MAP_TYPE_SATELLITE); //tipo de mapa, elegido: satelital
        mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(UNGS,19)); //se ubica en el mapa segun UNGS con zoom de 19. min=2 max=21
        mapa.setMyLocationEnabled(true); //visualizacion de la posicion con un triangulo azul
        mapa.getUiSettings().setZoomControlsEnabled(true); //configurar las acciones del interfaz de usuario
        mapa.getUiSettings().setCompassEnabled(true);
        //Agrego marcador de la UNGS
        mapa.addMarker(new MarkerOptions()
                .position(UNGS)
                .title("UNGS")
                .snippet("UNGS")
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_ungs)).anchor(0.5f, 0.5f));

        //Agrego marcador en el aula
        mapa.addMarker(new MarkerOptions().position(latLngAula)
                .title("AULA "+aula.getNumAula()).
                 snippet(Utils.ubicacionAula(aula.getNumAula()))
                .title("AULA "+aula.getNumAula() + "\n" + Utils.ubicacionAula(aula.getNumAula()))
                .icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_RED)));



        mapa.setOnMapClickListener(this); //escucha pulsaciones en la pantalla
    }

    public void moveCamera(View view)
    {
        //mapa.moveCamera(CameraUpdateFactory.newLatLng(UNGS));
        //se ubica en el mapa segun UNGS con zoom de 10. min=2 max=21
        mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(UNGS,18));
    }

    public void animateCamera(View view)
    {
        if (mapa.getMyLocation() != null)
            mapa.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng( mapa.getMyLocation().getLatitude(), mapa.getMyLocation().getLongitude()), 15));
    }

    public void addMarker(View view)
    {
        mapa.addMarker(new MarkerOptions().position(new LatLng(mapa.getCameraPosition().target.latitude,
        mapa.getCameraPosition().target.longitude)));
    }

    @Override
    public void onMapClick(LatLng puntoPulsado)
    {
        //mapa.addMarker(new MarkerOptions().position(puntoPulsado).icon(BitmapDescriptorFactory
        //.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
    }
}