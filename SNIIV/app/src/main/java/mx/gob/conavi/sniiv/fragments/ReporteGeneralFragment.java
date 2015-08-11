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
import mx.gob.conavi.sniiv.datos.DatosReporteGeneral;
import mx.gob.conavi.sniiv.modelos.Fechas;
import mx.gob.conavi.sniiv.modelos.ReporteGeneral;
import mx.gob.conavi.sniiv.parsing.ParseFechas;
import mx.gob.conavi.sniiv.parsing.ParseReporteGeneral;
import mx.gob.conavi.sniiv.sqlite.FechasRepository;
import mx.gob.conavi.sniiv.sqlite.ReporteGeneralRepository;

/**
 * Created by admin on 04/08/15.
 */
public class ReporteGeneralFragment extends BaseFragment {
    public static final String TAG = "ReporteGeneralFragment";

    private DatosReporteGeneral datos;
    private ReporteGeneral entidad;
    private Fechas fechas;
    private ReporteGeneralRepository repository;
    private FechasRepository fechasRepository;
    private boolean errorRetrievingData = false;

    @Bind(R.id.txtAccionesFinan) TextView txtAccFinan;
    @Bind(R.id.txtMontoFinan) TextView txtMtoFinan;
    @Bind(R.id.txtAccionesSub) TextView txtAccSub;
    @Bind(R.id.txtMontoSub) TextView txtMtoSub;
    @Bind(R.id.txtVigentes) TextView txtVigentes;
    @Bind(R.id.txtRegistradas) TextView txtRegistradas;
    @Bind(R.id.txtTitleFinanciamientos) TextView txtTitleFinanciamientos;
    @Bind(R.id.txtTitleSubsidios) TextView txtTitleSubsidios;
    @Bind(R.id.txtTitleOfertaVivienda) TextView txtTitleOferta;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        repository = new ReporteGeneralRepository(getActivity());
        fechasRepository = new FechasRepository(getActivity());
        valueChangeListener = configuraValueChangeListener();
    }

    @Override
    protected String getKey() {
        return ReporteGeneral.TABLE;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_reportegeneral, container, false);
        ButterKnife.bind(this, rootView);

        pickerEstados = (NumberPicker) rootView.findViewById(R.id.pckEstados);
        configuraPickerView();

        return rootView;
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

    protected void loadFromStorage() {
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

    public void mostrarDatos() {
        if(entidad != null) {
            txtAccFinan.setText(Utils.toStringDivide(entidad.getAcc_finan()));
            txtMtoFinan.setText(Utils.toStringDivide(entidad.getMto_finan(), 1000000));
            txtAccSub.setText(Utils.toStringDivide(entidad.getAcc_subs()));
            txtMtoSub.setText(Utils.toStringDivide(entidad.getMto_subs(), 1000000));
            txtVigentes.setText(Utils.toStringDivide(entidad.getVv()));
            txtRegistradas.setText(Utils.toStringDivide(entidad.getVr()));
        }

        if(fechas != null) {
            String financiamientos = String.format("%s (%s)", getString(R.string.title_financiamiento),
                    fechas.getFecha_finan());
            String subsidios = String.format("%s (%s)", getString(R.string.title_subsidios),
                    fechas.getFecha_subs());
            String oferta = String.format("%s (%s)", getString(R.string.title_oferta_vivienda),
                    fechas.getFecha_vv());
            txtTitleFinanciamientos.setText(financiamientos);
            txtTitleSubsidios.setText(subsidios);
            txtTitleOferta.setText(oferta);
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

                saveTimeLastUpdated();

                ParseFechas parseFechas = new ParseFechas();
                Fechas[] fechasWeb = parseFechas.getDatos();
                fechasRepository.deleteAll();
                fechasRepository.saveAll(fechasWeb);

                Fechas[] fechasStorage = fechasRepository.loadFromStorage();
                if(fechasStorage.length > 0) {
                    fechas = fechasStorage[0];
                }
            } catch (Exception e) {
                Log.v(TAG, "Error obteniendo datos " + e.getMessage());
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
