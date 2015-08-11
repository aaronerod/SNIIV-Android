package mx.gob.conavi.sniiv.fragments;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import mx.gob.conavi.sniiv.datos.DatosValorVivienda;
import mx.gob.conavi.sniiv.modelos.AvanceObra;
import mx.gob.conavi.sniiv.modelos.ValorVivienda;
import mx.gob.conavi.sniiv.parsing.ParseAvanceObra;
import mx.gob.conavi.sniiv.parsing.ParseValorVivienda;
import mx.gob.conavi.sniiv.sqlite.AvanceObraRepository;
import mx.gob.conavi.sniiv.sqlite.ValorViviendaRepository;

/**
 * Created by admin on 04/08/15.
 */
public class ValorViviendaFragment extends BaseFragment {
    public static final String TAG = "ValorViviendaFragment";

    private DatosValorVivienda datos;
    private ValorVivienda entidad;
    private ValorViviendaRepository repository;
    private boolean errorRetrievingData = false;

    @Bind(R.id.txtEconomica) TextView txtEconomica;
    @Bind(R.id.txtPopular) TextView txtPopular;
    @Bind(R.id.txtTradicional) TextView txtTradicional;
    @Bind(R.id.txtMedia) TextView txtMedia;
    @Bind(R.id.txtTotal) TextView txtTotal;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        repository = new ValorViviendaRepository(getActivity());
        scrollListener = configuraScrollListener();
    }

    protected void loadFromStorage() {
        ValorVivienda[] datosStorage = repository.loadFromStorage();
        if(datosStorage.length > 0) {
            datos = new DatosValorVivienda(getActivity(), datosStorage);
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
        View rootView = inflater.inflate(R.layout.fragment_valor_vivienda, container, false);
        ButterKnife.bind(this, rootView);

        pickerEstados = (NumberPicker) rootView.findViewById(R.id.pckEstados);
        configuraPickerView();

        return rootView;
    }

    protected void mostrarDatos() {
        if(entidad != null) {
            txtEconomica.setText(Utils.toString(entidad.getEconomica()));
            txtPopular.setText(Utils.toString(entidad.getPopular()));
            txtTradicional.setText(Utils.toString(entidad.getTradicional()));
            txtMedia.setText(Utils.toString(entidad.getMedia_residencial()));
            txtTotal.setText(Utils.toString(entidad.getTotal()));
        }
    }

    @Override
    protected NumberPicker.OnScrollListener configuraScrollListener() {
        return new NumberPicker.OnScrollListener() {
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
    }

    @Override
    protected AsyncTask<Void, Void, Void> getAsyncTask() {
        return new AsyncTaskRunner();
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
                ParseValorVivienda parse = new ParseValorVivienda();
                ValorVivienda[] datosParse = parse.getDatos();
                repository.deleteAll();
                repository.saveAll(datosParse);

                datos = new DatosValorVivienda(getActivity(), datosParse);
                entidad = datos.consultaNacional();
            } catch (Exception e) {
                Log.v(TAG, "Error obteniendo datos");
                errorRetrievingData = true;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void s) {
            if (!errorRetrievingData) {
                habilitaPantalla();
            } else {
                Utils.alertDialogShow(getActivity(), getString(R.string.mensaje_error_datos));
                progressDialog.dismiss();
            }
        }
    }
}
