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
import mx.gob.conavi.sniiv.datos.DatosPCU;
import mx.gob.conavi.sniiv.modelos.PCU;
import mx.gob.conavi.sniiv.parsing.ParsePCU;
import mx.gob.conavi.sniiv.sqlite.PCURepository;


public class PCUFragment extends BaseFragment {
    public static final String TAG = "PCUFragment";

    private DatosPCU datos;
    private PCU entidad;
    private PCURepository repository;

    @Bind(R.id.txtTitlePCU) TextView txtTitlePCU;
    @Bind(R.id.txtU1) TextView txtU1;
    @Bind(R.id.txtU2) TextView txtU2;
    @Bind(R.id.txtU3) TextView txtU3;
    @Bind(R.id.txtND) TextView txtND;
    @Bind(R.id.txtTotal) TextView txtTotal;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        repository = new PCURepository(getActivity());
        valueChangeListener = configuraValueChangeListener();
    }

    protected void loadFromStorage() {
        PCU[] datosStorage = repository.loadFromStorage();
        if(datosStorage.length > 0) {
            datos = new DatosPCU(getActivity(), datosStorage);
            entidad = datos.consultaNacional();
            pickerEstados.setEnabled(true);
        } else {
            Utils.alertDialogShow(getActivity(), getString(R.string.no_conectado));
        }

        asignaFechas();

        mostrarDatos();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pcu, container, false);
        ButterKnife.bind(this, rootView);

        pickerEstados = (NumberPicker) rootView.findViewById(R.id.pckEstados);
        configuraPickerView();

        return rootView;
    }

    protected void mostrarDatos() {
        if(entidad != null) {
            txtU1.setText(Utils.toString(entidad.getU1()));
            txtU2.setText(Utils.toString(entidad.getU2()));
            txtU3.setText(Utils.toString(entidad.getU3()));
            txtND.setText(Utils.toString(entidad.getNd()));
            txtTotal.setText(Utils.toString(entidad.getTotal()));
        }

        if (fechas != null) {
            String pcu = String.format("%s (%s)", getString(R.string.title_pcu),
                    fechas.getFecha_vv());
            txtTitlePCU.setText(pcu);
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
        return PCU.TABLE;
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
                ParsePCU parse = new ParsePCU();
                PCU[] datosParse = parse.getDatos();
                repository.deleteAll();
                repository.saveAll(datosParse);

                datos = new DatosPCU(getActivity(), datosParse);
                entidad = datos.consultaNacional();

                saveTimeLastUpdated();

                obtenerFechas();
            } catch (Exception e) {
                Log.v(TAG, "Error obteniendo datos");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void s) {
            habilitaPantalla();
        }
    }
}
