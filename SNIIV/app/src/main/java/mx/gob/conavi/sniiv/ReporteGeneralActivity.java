package mx.gob.conavi.sniiv;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import butterknife.Bind;
import butterknife.ButterKnife;
import mx.gob.conavi.sniiv.Utils.Utils;
import mx.gob.conavi.sniiv.datos.DatosReporteGeneral;
import mx.gob.conavi.sniiv.datos.Entidad;
import mx.gob.conavi.sniiv.parsing.ParseReporteGeneral;
import mx.gob.conavi.sniiv.parsing.ReporteGeneral;
import mx.gob.conavi.sniiv.sqlite.ReporteGeneralRepository;

import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.TextView;

public class ReporteGeneralActivity extends Activity implements AdapterView.OnItemSelectedListener, NumberPicker.OnClickListener {

    public static final String TAG = "ReporteGeneralActivity";
    private NumberPicker pickerEstados;
    private DatosReporteGeneral datos;
    private ReporteGeneral entidad;
    private ReporteGeneralRepository repository = new ReporteGeneralRepository(this);
    protected ProgressDialog progressDialog;

    @Bind(R.id.txtAccionesFinan) TextView txtAccFinan;
    @Bind(R.id.txtMontoFinan) TextView txtMtoFinan;
    @Bind(R.id.txtAccionesSub) TextView txtAccSub;
    @Bind(R.id.txtMontoSub) TextView txtMtoSub;
    @Bind(R.id.txtVigentes) TextView txtVigentes;
    @Bind(R.id.txtregistradas) TextView txtRegistradas;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reportegeneral);
        ButterKnife.bind(this);

        String[] edos = Utils.listEdo;
        pickerEstados = (NumberPicker) findViewById(R.id.pckEstados);
        pickerEstados.setMaxValue(edos.length - 1);
        pickerEstados.setMinValue(0);
        pickerEstados.setDisplayedValues(edos);
        pickerEstados.setOnClickListener(this);

        pickerEstados.setOnValueChangedListener(new OnValueChangeListener() {

            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                Log.v(TAG, oldVal + " " + newVal);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        /*if (Utils.isNetworkAvailable(this)) {
            new AsyncTaskRunner().execute();
            return;
        }*/

        loadFromStorage();
    }

    private void loadFromStorage() {
        ReporteGeneral[] datosStorage = repository.loadFromStorage();
        if(datosStorage.length > 0) {
            datos = new DatosReporteGeneral(this, datosStorage);
            entidad = datos.consultaNacional();
        } else {
            Utils.alertDialogShow(this, getString(R.string.no_conectado));
        }

        mostrarDatos();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    @Override
    public void onClick(View v) {
    }

    public void mostrarDatos() {
        if(entidad != null) {
            txtAccFinan.setText(Utils.toStringDivide(entidad.getAcc_finan()));
            txtMtoFinan.setText(Utils.toStringDivide(entidad.getMto_finan(), 1000000));
            txtAccSub.setText(Utils.toStringDivide(entidad.getAcc_subs()));
            txtMtoSub.setText(Utils.toStringDivide(entidad.getMto_subs(), 1000000));
            txtVigentes.setText(Utils.toStringDivide(entidad.getVv()));
            txtRegistradas.setText(Utils.toStringDivide(entidad.getVr()));
        }
    }

    private class AsyncTaskRunner extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(ReporteGeneralActivity.this,
                    null, getString(R.string.mensaje_espera));
        }

        @Override
        protected Void doInBackground(Void... params) {
            ParseReporteGeneral parse = new ParseReporteGeneral();
            ReporteGeneral[] reportes = parse.getDatos();
            repository.deleteAll();
            repository.saveAll(reportes);

            datos = new DatosReporteGeneral(ReporteGeneralActivity.this, reportes);
            entidad = datos.consultaNacional();
            return null;
        }

        @Override
        protected void onPostExecute(Void s) {
            mostrarDatos();
            progressDialog.dismiss();
        }
    }
}
