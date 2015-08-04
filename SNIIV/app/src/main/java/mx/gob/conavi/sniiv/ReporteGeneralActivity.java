package mx.gob.conavi.sniiv;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.NumberPicker;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import mx.gob.conavi.sniiv.Utils.Utils;
import mx.gob.conavi.sniiv.datos.DatosReporteGeneral;
import mx.gob.conavi.sniiv.parsing.ParseReporteGeneral;
import mx.gob.conavi.sniiv.parsing.ReporteGeneral;
import mx.gob.conavi.sniiv.sqlite.ReporteGeneralRepository;

public class ReporteGeneralActivity extends AppCompatActivity {

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
    @Bind(R.id.txtRegistradas) TextView txtRegistradas;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reportegeneral);
        ButterKnife.bind(this);

        String[] edos = Utils.listEdo;
        pickerEstados = (NumberPicker) findViewById(R.id.pckEstados);
        pickerEstados.setMaxValue(edos.length - 1);
        pickerEstados.setMinValue(0);
        pickerEstados.setDisplayedValues(edos);
        pickerEstados.setOnScrollListener(scrollListener);
        pickerEstados.setEnabled(false);
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
            pickerEstados.setEnabled(true);
        } else {
            Utils.alertDialogShow(this, getString(R.string.no_conectado));
        }

        mostrarDatos();
    }

    private NumberPicker.OnScrollListener scrollListener = new NumberPicker.OnScrollListener() {
        @Override
        public void onScrollStateChange(NumberPicker picker, int scrollState) {
            if(scrollState != NumberPicker.OnScrollListener.SCROLL_STATE_IDLE) { return; }

            int valor = picker.getValue();
            if (valor == 0) {
                entidad = datos.consultaNacional();
            } else {
                entidad = datos.consultaEntidad(valor);
            }

            mostrarDatos();
        }
    };

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
            pickerEstados.setEnabled(true);
            progressDialog.dismiss();
        }
    }
}
