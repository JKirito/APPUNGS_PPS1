package com.pps1.guiame.guiame.controlador;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.pps1.guiame.guiame.R;
import com.pps1.guiame.guiame.dto.Aula;
import com.pps1.guiame.guiame.dto.Curso;
import com.pps1.guiame.guiame.persistencia.dao.AulaDAO;

import java.io.IOException;

/**
 * Created by javi on 01/06/15.
 */
public class Geolocalizador extends Activity {

    private int posicionItemSeleccionado;
    private ListView listaCursos;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
    }

    public Geolocalizador(int posicionItemSeleccionado, ListView listaCursos)
    {
        this.posicionItemSeleccionado = posicionItemSeleccionado;
        this.listaCursos = listaCursos;
    }

    public void geoLocalizarAula()
    {
        final Curso itemSeleccionado = (Curso) listaCursos.getAdapter().getItem(posicionItemSeleccionado);
        dialog.setMessage("Buscando aula...");
        dialog.show();
        Thread tr = new Thread()
        {
            @Override
            public void run()
            {
                final Aula aula;
                try {
                    aula = new AulaDAO().getAula(itemSeleccionado.getAula());
                } catch (IOException e) {
                    e.printStackTrace();
                    runOnUiThread(
                            new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(),
                                            "Hubo un error al obtener el aula, intente de nuevo.", Toast.LENGTH_SHORT).show();
                                }
                            });
                    return;
                }
                if(aula == null || aula.getLatitud() == 0.0 || aula.getLongitud() == 0.0)
                {
                    runOnUiThread(
                            new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    String msj = aula == null ? "No se ha podido localizar el aula" : "No se tiene su ubicación :(";
                                    Toast.makeText(getApplicationContext(), msj, Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                            });
                    return;
                }

                Bundle bundleBuscAula = new Bundle();
                bundleBuscAula.putSerializable("Aula", aula);

                Intent intent = new Intent(getApplicationContext(), Mapa.class);
                intent.putExtras(bundleBuscAula);
                startActivity(intent);
                dialog.dismiss();
                //finish();
            }
        };
        tr.start();
    }
}
