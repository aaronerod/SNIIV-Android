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

import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import mx.gob.conavi.sniiv.R;
import mx.gob.conavi.sniiv.Utils.Utils;
import mx.gob.conavi.sniiv.activities.SniivApplication;
import mx.gob.conavi.sniiv.datos.DatosTipoVivienda;
import mx.gob.conavi.sniiv.modelos.TipoVivienda;
import mx.gob.conavi.sniiv.parsing.ParseTipoVivienda;
import mx.gob.conavi.sniiv.sqlite.TipoViviendaRepository;

/**
 * Created by admin on 04/08/15.
 */
public class TipoViviendaFragment extends BaseFragment {
    public static final String TAG = "TipoViviendaFragment";

    private DatosTipoVivienda datos;
    private TipoVivienda entidad;
    private TipoViviendaRepository repository;
    private boolean errorRetrievingData = false;

    @Bind(R.id.txtHorizontal) TextView txtHorizontal;
    @Bind(R.id.txtVertical) TextView txtVertical;
    @Bind(R.id.txtTotal) TextView txtTotal;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        repository = new TipoViviendaRepository(getActivity());
        valueChangeListener = configuraValueChangeListener();
    }

    protected void loadFromStorage() {
        TipoVivienda[] datosStorage = repository.loadFromStorage();
        if(datosStorage.length > 0) {
            datos = new DatosTipoVivienda(getActivity(), datosStorage);
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
        View rootView = inflater.inflate(R.layout.fragment_tipo_vivienda, container, false);
        ButterKnife.bind(this, rootView);

        pickerEstados = (NumberPicker) rootView.findViewById(R.id.pckEstados);
        configuraPickerView();

        return rootView;
    }

    protected void mostrarDatos() {
        if(entidad != null) {
            txtHorizontal.setText(Utils.toString(entidad.getHorizontal()));
            txtVertical.setText(Utils.toString(entidad.getVertical()));
            txtTotal.setText(Utils.toString(entidad.getTotal()));
        }
    }

    @Override
    protected NumberPicker.OnValueChangeListener configuraValueChangeListener() {
        return new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                if (newVal == 0) {
                    entidad = datos.consultaNacional();
                } else {
                    entidad = datos.consultaEntidad(newVal);
                }

                mostrarDatos();
            }
        };
    }

    @Override
    protected AsyncTask<Void, Void, Void> getAsyncTask() {
        return new AsyncTaskRunner();
    }

    @Override
    protected String getKey() {
        return TipoVivienda.TABLE;
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
                ParseTipoVivienda parse = new ParseTipoVivienda();
                TipoVivienda[] datosParse = parse.getDatos();
                repository.deleteAll();
                repository.saveAll(datosParse);

                datos = new DatosTipoVivienda(getActivity(), datosParse);
                entidad = datos.consultaNacional();

                SniivApplication app = (SniivApplication)getActivity().getApplicationContext();
                app.setTimeLastUpdated(getKey(), new Date().getTime());
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
