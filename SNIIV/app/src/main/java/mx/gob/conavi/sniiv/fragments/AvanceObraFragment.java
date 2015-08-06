package mx.gob.conavi.sniiv.fragments;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import mx.gob.conavi.sniiv.R;
import mx.gob.conavi.sniiv.Utils.Utils;
import mx.gob.conavi.sniiv.datos.DatosAvanceObra;
import mx.gob.conavi.sniiv.modelos.AvanceObra;
import mx.gob.conavi.sniiv.parsing.ParseAvanceObra;
import mx.gob.conavi.sniiv.sqlite.AvanceObraRepository;

/**
 * Created by admin on 04/08/15.
 */
public class AvanceObraFragment extends Fragment {
    public static final String TAG = "AvanceObraFragment";

    private DatosAvanceObra datos;
    private AvanceObra entidad;
    private AvanceObraRepository repository;
    protected ProgressDialog progressDialog;

    @Bind(R.id.txtProceso50) TextView txtProceso50;
    @Bind(R.id.txtProceso99) TextView txtProceso99;
    @Bind(R.id.txtTerminadasRecientes) TextView txtTerminadasRecientes;
    @Bind(R.id.txtTerminadasAntiguas) TextView txtTerminadasAntiguas;
    @Bind(R.id.txtTotal) TextView txtTotal;
    @Bind(R.id.pckEstados) NumberPicker pickerEstados;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        repository = new AvanceObraRepository(getActivity());
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

    protected void loadFromStorage() {
        AvanceObra[] datosStorage = repository.loadFromStorage();
        if(datosStorage.length > 0) {
            datos = new DatosAvanceObra(getActivity(), datosStorage);
            entidad = datos.consultaNacional();
            pickerEstados.setEnabled(true);
        } else {
            Utils.alertDialogShow(getActivity(), getString(R.string.no_conectado));
        }

        mostrarDatos();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_avance_obra, container, false);
        ButterKnife.bind(this, rootView);

        pickerEstados.setMaxValue(Utils.listEdo.length - 1);
        pickerEstados.setMinValue(0);
        pickerEstados.setDisplayedValues(Utils.listEdo);
        pickerEstados.setOnScrollListener(scrollListener);
        pickerEstados.setEnabled(false);

        return rootView;
    }

    protected NumberPicker.OnScrollListener scrollListener = new NumberPicker.OnScrollListener() {
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

    protected void mostrarDatos() {
        if(entidad != null) {
            txtProceso50.setText(Utils.toString(entidad.getViv_proc_m50()));
            txtProceso99.setText(Utils.toString(entidad.getViv_proc_50_99()));
            txtTerminadasRecientes.setText(Utils.toString(entidad.getViv_term_rec()));
            txtTerminadasAntiguas.setText(Utils.toString(entidad.getViv_term_ant()));
            txtTotal.setText(Utils.toString(entidad.getTotal()));
        }
    }

    protected class AsyncTaskRunner extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(getActivity(),
                    null, getString(R.string.mensaje_espera));
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                ParseAvanceObra parse = new ParseAvanceObra();
                AvanceObra[] datosParse = parse.getDatos();
                Log.v(TAG, "Length: " + datosParse.length);
                repository.deleteAll();
                repository.saveAll(datosParse);

                datos = new DatosAvanceObra(getActivity(), datosParse);
                entidad = datos.consultaNacional();
            } catch (Exception e) {
                Log.v(TAG, "Error obteniendo datos");
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
