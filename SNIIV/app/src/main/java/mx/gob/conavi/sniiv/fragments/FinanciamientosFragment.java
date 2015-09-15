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
import mx.gob.conavi.sniiv.datos.DatosFinanciamiento;
import mx.gob.conavi.sniiv.modelos.ConsultaFinanciamiento;
import mx.gob.conavi.sniiv.modelos.Financiamiento;
import mx.gob.conavi.sniiv.parsing.ParseFinanciamiento;
import mx.gob.conavi.sniiv.sqlite.FinanciamientoRepository;


public class FinanciamientosFragment extends BaseFragment {
    public static final String TAG = "FinanciamientosFragment";

    private DatosFinanciamiento datos;
    private ConsultaFinanciamiento entidad;
    private FinanciamientoRepository repository;


    @Bind(R.id.txtNuevasCofinAcc) TextView txtNuevasCofinAcc;
    @Bind(R.id.txtNuevasCofinMto) TextView txtNuevasCofinMto;
    @Bind(R.id.txtNuevasCredAcc) TextView txtNuevasCredAcc;
    @Bind(R.id.txtNuevasCredMto) TextView txtNuevasCredMto;

    @Bind(R.id.txtUsadasCofinAcc) TextView txtUsadasCofinAcc;
    @Bind(R.id.txtUsadasCofinMto) TextView txtUsadasCofinMto;
    @Bind(R.id.txtUsadasCredAcc) TextView txtUsadasCredAcc;
    @Bind(R.id.txtUsadasCredMto) TextView txtUsadasCredMto;

    @Bind(R.id.txtMejoramientoCofinAcc) TextView txtMejoramientoCofinAcc;
    @Bind(R.id.txtMejoramientoCofinMto) TextView txtMejoramientoCofinMto;
    @Bind(R.id.txtMejoramientoCrediAcc) TextView txtMejoramientoCrediAcc;
    @Bind(R.id.txtMejoramientoCrediMto) TextView txtMejoramientoCrediMto;

    @Bind(R.id.txtOtrosAcc) TextView txtOtrosAcc;
    @Bind(R.id.txtOtrosMto) TextView txtOtrosMto;

    @Bind(R.id.txtTotalAcc) TextView txtTotalAcc;
    @Bind(R.id.txtTotalMto) TextView txtTotalMto;

    @Bind(R.id.txtTitleSubsidios) TextView txtTitleFinanciamientos;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        repository = new FinanciamientoRepository(getActivity());
        valueChangeListener = configuraValueChangeListener();
    }

    protected void loadFromStorage() {
        Financiamiento[] datosStorage = repository.loadFromStorage();
        if(datosStorage.length > 0) {
            datos = new DatosFinanciamiento(getActivity(), datosStorage);
            entidad = datos.consultaNacional();
            pickerEstados.setEnabled(true);
        } else {
            Utils.alertDialogShow(getActivity(), getString(R.string.no_conectado));
        }

        loadFechasStorage();

        mostrarDatos();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_financiamientos, container, false);
        ButterKnife.bind(this, rootView);

        pickerEstados = (NumberPicker) rootView.findViewById(R.id.pckEstados);
        configuraPickerView();
        return rootView;
    }

    protected void mostrarDatos() {
        if(entidad != null) {
            txtNuevasCofinAcc.setText(Utils.toStringDivide(entidad.getViviendasNuevas().getCofinanciamiento().getAcciones()));
            txtNuevasCofinMto.setText(Utils.toStringDivide(entidad.getViviendasNuevas().getCofinanciamiento().getMonto(),1000000));
            txtNuevasCredAcc.setText(Utils.toStringDivide(entidad.getViviendasNuevas().getCreditoIndividual().getAcciones()));
            txtNuevasCredMto.setText(Utils.toStringDivide(entidad.getViviendasNuevas().getCreditoIndividual().getMonto(), 1000000));
            txtUsadasCofinAcc.setText(Utils.toStringDivide(entidad.getViviendasUsadas().getCofinanciamiento().getAcciones()));
            txtUsadasCofinMto.setText(Utils.toStringDivide(entidad.getViviendasUsadas().getCofinanciamiento().getMonto(), 1000000));
            txtUsadasCredAcc.setText(Utils.toStringDivide(entidad.getViviendasUsadas().getCreditoIndividual().getAcciones()));
            txtUsadasCredMto.setText(Utils.toStringDivide(entidad.getViviendasUsadas().getCreditoIndividual().getMonto(), 1000000));
            txtMejoramientoCofinAcc.setText(Utils.toStringDivide(entidad.getMejoramientos().getCofinanciamiento().getAcciones()));
            txtMejoramientoCofinMto.setText(Utils.toStringDivide(entidad.getMejoramientos().getCofinanciamiento().getMonto(), 1000000));
            txtMejoramientoCrediAcc.setText(Utils.toStringDivide(entidad.getMejoramientos().getCreditoIndividual().getAcciones()));
            txtMejoramientoCrediMto.setText(Utils.toStringDivide(entidad.getMejoramientos().getCreditoIndividual().getMonto(), 1000000));
            txtOtrosAcc.setText(Utils.toStringDivide(entidad.getOtrosProgramas().getCreditoIndividual().getAcciones()));
            txtOtrosMto.setText(Utils.toStringDivide(entidad.getOtrosProgramas().getCreditoIndividual().getMonto(), 1000000));
            txtTotalAcc.setText(Utils.toStringDivide(entidad.getTotal().getAcciones()));
            txtTotalMto.setText(Utils.toStringDivide(entidad.getTotal().getMonto(), 1000000));
        }

        if (fechas != null) {
            String pcu = String.format("%s (%s)", getString(R.string.title_financiamiento),
                    Utils.formatoDiaMes(fechas.getFecha_finan()));
            txtTitleFinanciamientos.setText(pcu);
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
        return Financiamiento.TABLE;
    }

    @Override
    protected String getFechaAsString() {
        return fechas != null ? fechas.getFecha_finan() : null;
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
                ParseFinanciamiento parse = new ParseFinanciamiento();
                Financiamiento[] datosParse = parse.getDatos();
                repository.deleteAll();
                repository.saveAll(datosParse);

                datos = new DatosFinanciamiento(getActivity(), datosParse);
                entidad = datos.consultaNacional();
                Log.v(TAG, "entidad" + entidad.toString());

                saveTimeLastUpdated(getFechaActualizacion().getTime());

                loadFechasStorage();
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
