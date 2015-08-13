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
import mx.gob.conavi.sniiv.datos.DatosSubsidio;
import mx.gob.conavi.sniiv.modelos.ConsultaSubsidio;
import mx.gob.conavi.sniiv.modelos.PCU;
import mx.gob.conavi.sniiv.modelos.Subsidio;
import mx.gob.conavi.sniiv.parsing.ParsePCU;
import mx.gob.conavi.sniiv.parsing.ParseSubsidio;
import mx.gob.conavi.sniiv.sqlite.PCURepository;
import mx.gob.conavi.sniiv.sqlite.SubsidioRepository;


public class SubsidiosFragment extends BaseFragment {
    public static final String TAG = "SubsidiosFragment";

    private DatosSubsidio datos;
    private ConsultaSubsidio entidad;
    private SubsidioRepository repository;


    @Bind(R.id.txtNuevaAcc) TextView txtNuevaAcc;
    @Bind(R.id.txtNuevaMto) TextView txtNuevaMto;
    @Bind(R.id.txtUsadaAcc) TextView txtUsadaAcc;
    @Bind(R.id.txtUsadaMto) TextView txtUsadaMto;
    @Bind(R.id.txtAutoproduccionAcc) TextView txtAutoproduccionAcc;
    @Bind(R.id.txtAutoproduccionMto) TextView txtAutoproduccionMto;
    @Bind(R.id.txtMejoramientoAcc) TextView txtMejoramientoAcc;
    @Bind(R.id.txtMejoramientoMto) TextView txtMejoramientoMto;
    @Bind(R.id.txtLotesAcc) TextView txtLotesAcc;
    @Bind(R.id.txtLotesMto) TextView txtLotesMto;
    @Bind(R.id.txtOtrosAcc) TextView txtOtrosAcc;
    @Bind(R.id.txtOtrosMto) TextView txtOtrosMto;
    @Bind(R.id.txtTotalAcc) TextView txtTotalAcc;
    @Bind(R.id.txtTotalMto) TextView txtTotalMto;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        repository = new SubsidioRepository(getActivity());
        valueChangeListener = configuraValueChangeListener();
    }

    protected void loadFromStorage() {
        Subsidio[] datosStorage = repository.loadFromStorage();
        if(datosStorage.length > 0) {
            datos = new DatosSubsidio(getActivity(), datosStorage);
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
        View rootView = inflater.inflate(R.layout.fragment_subsidios, container, false);
        ButterKnife.bind(this, rootView);

        pickerEstados = (NumberPicker) rootView.findViewById(R.id.pckEstados);
        configuraPickerView();

        return rootView;
    }

    protected void mostrarDatos() {
        if(entidad != null) {



            txtNuevaAcc.setText(Utils.toString(entidad.getNueva().getAcciones()));
            txtNuevaMto.setText(Utils.toString(entidad.getNueva().getMonto()));
            txtUsadaAcc.setText(Utils.toString(entidad.getUsada().getAcciones()));
            txtUsadaMto.setText(Utils.toString(entidad.getUsada().getMonto()));
            txtAutoproduccionAcc.setText(Utils.toString(entidad.getAutoproduccion().getAcciones()));
            txtAutoproduccionMto.setText(Utils.toString(entidad.getAutoproduccion().getMonto()));
            txtMejoramientoAcc.setText(Utils.toString(entidad.getMejoramiento().getAcciones()));
            txtMejoramientoMto.setText(Utils.toString(entidad.getMejoramiento().getMonto()));

            txtLotesAcc.setText(Utils.toString(entidad.getLotes().getAcciones()));
            txtLotesMto.setText(Utils.toString(entidad.getLotes().getMonto()));

            txtOtrosAcc.setText(Utils.toString(entidad.getOtros().getAcciones()));
            txtOtrosMto.setText(Utils.toString(entidad.getOtros().getMonto()));

            txtTotalAcc.setText(Utils.toString(entidad.getTotal().getAcciones()));
            txtTotalMto.setText(Utils.toString(entidad.getTotal().getMonto()));
        }

        if (fechas != null) {
            //String pcu = String.format("%s (%s)", getString(R.string.title_pcu),
                //    fechas.getFecha_vv());
            //txtTitlePCU.setText(pcu);
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
                ParseSubsidio parse = new ParseSubsidio();
                Subsidio[] datosParse = parse.getDatos();
                repository.deleteAll();
                repository.saveAll(datosParse);

                datos = new DatosSubsidio(getActivity(), datosParse);
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
