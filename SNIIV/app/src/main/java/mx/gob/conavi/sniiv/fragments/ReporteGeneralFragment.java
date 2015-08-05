package mx.gob.conavi.sniiv.fragments;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import mx.gob.conavi.sniiv.R;
import mx.gob.conavi.sniiv.Utils.Utils;
import mx.gob.conavi.sniiv.datos.DatosReporteGeneral;
import mx.gob.conavi.sniiv.parsing.ParseReporteGeneral;
import mx.gob.conavi.sniiv.parsing.ReporteGeneral;
import mx.gob.conavi.sniiv.sqlite.ReporteGeneralRepository;

/**
 * Created by admin on 04/08/15.
 */
public class ReporteGeneralFragment extends Fragment {
    public static final String TAG = "ReporteGeneralFragment";

    private DatosReporteGeneral datos;
    private ReporteGeneral entidad;
    private ReporteGeneralRepository repository;
    protected ProgressDialog progressDialog;

    @Bind(R.id.txtAccionesFinan) TextView txtAccFinan;
    @Bind(R.id.txtMontoFinan) TextView txtMtoFinan;
    @Bind(R.id.txtAccionesSub) TextView txtAccSub;
    @Bind(R.id.txtMontoSub) TextView txtMtoSub;
    @Bind(R.id.txtVigentes) TextView txtVigentes;
    @Bind(R.id.txtRegistradas) TextView txtRegistradas;
    @Bind(R.id.pckEstados) NumberPicker pickerEstados;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        repository = new ReporteGeneralRepository(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_reportegeneral, container, false);
        ButterKnife.bind(this, rootView);

        pickerEstados.setMaxValue(Utils.listEdo.length - 1);
        pickerEstados.setMinValue(0);
        pickerEstados.setDisplayedValues(Utils.listEdo);
        pickerEstados.setOnScrollListener(scrollListener);
        pickerEstados.setEnabled(false);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (Utils.isNetworkAvailable(getActivity())) {
            new AsyncTaskRunner().execute();
            return;
        }

        loadFromStorage();
    }

    private void loadFromStorage() {
        ReporteGeneral[] datosStorage = repository.loadFromStorage();
        if(datosStorage.length > 0) {
            datos = new DatosReporteGeneral(getActivity(), datosStorage);
            entidad = datos.consultaNacional();
            pickerEstados.setEnabled(true);
        } else {
            Utils.alertDialogShow(getActivity(), getString(R.string.no_conectado));
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
            progressDialog = ProgressDialog.show(getActivity(),
                    null, getString(R.string.mensaje_espera));
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                ParseReporteGeneral parse = new ParseReporteGeneral();
                ReporteGeneral[] reportes = parse.getDatos();
                repository.deleteAll();
                repository.saveAll(reportes);

                datos = new DatosReporteGeneral(getActivity(), reportes);
                entidad = datos.consultaNacional();
            } catch (Exception e) {
                Toast.makeText(getActivity(), R.string.mensaje_error, Toast.LENGTH_LONG).show();
            }

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
