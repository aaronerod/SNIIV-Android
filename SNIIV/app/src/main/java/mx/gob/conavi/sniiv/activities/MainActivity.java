package mx.gob.conavi.sniiv.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import mx.gob.conavi.sniiv.R;
import mx.gob.conavi.sniiv.Utils.Utils;
import mx.gob.conavi.sniiv.datos.DatosReporteGeneral;
import mx.gob.conavi.sniiv.modelos.Fechas;
import mx.gob.conavi.sniiv.modelos.ReporteGeneral;
import mx.gob.conavi.sniiv.parsing.ParseFechas;
import mx.gob.conavi.sniiv.parsing.ParseFechasWeb;
import mx.gob.conavi.sniiv.parsing.ParseReporteGeneral;
import mx.gob.conavi.sniiv.sqlite.FechasRepository;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    ListView lvwMenu;
    ArrayAdapter<String> arrayAdapter;
    ArrayList<String> listMenu;
    protected FechasRepository fechasRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        setContentView(R.layout.activity_main);
        lvwMenu = (ListView) findViewById(R.id.lvwMenu);
        listMenu= Utils.listMenu;
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,listMenu);
        lvwMenu.setAdapter(arrayAdapter);
        lvwMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
                changeActivity(position);
            }
        });

        fechasRepository = new FechasRepository(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (Utils.isNetworkAvailable(this)) {
            new AsyncTaskRunner().execute();
        }
    }

    protected void cargarDatosFechas() {
        Log.v(TAG, "carga fechas");
        ParseFechasWeb parseFechasWeb = new ParseFechasWeb();
        Fechas[] fechas = parseFechasWeb.getDatos();
        fechasRepository.deleteAll();
        fechasRepository.saveAll(fechas);
    }

    public void changeActivity(int position) {
        Intent intent=null;

        if (position == 0) {
            intent=new Intent(this, ReporteGeneralActivity.class);
        } else if (position == 1) {
            intent=new Intent(this, OfertaActivity.class);
        } else if (position == 2)
        {
            intent=new Intent(this, DemandaActivity.class);
        }

        startActivity(intent);
    }

    private class AsyncTaskRunner extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                cargarDatosFechas();
            } catch (Exception e) {
                Log.v(TAG, "Error obteniendo fechas " + e.getMessage());
            }

            return null;
        }
    }
}
