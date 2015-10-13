package mx.gob.conavi.sniiv.fragments.demanda;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.NumberPicker;

import java.util.ArrayList;
import java.util.EnumSet;

import mx.gob.conavi.sniiv.R;
import mx.gob.conavi.sniiv.Utils.Utils;
import mx.gob.conavi.sniiv.charts.PieChartBuilder;
import mx.gob.conavi.sniiv.datos.DatosFinanciamiento;
import mx.gob.conavi.sniiv.listeners.OnChartValueSelectedMillones;
import mx.gob.conavi.sniiv.modelos.demanda.ConsultaFinanciamiento;
import mx.gob.conavi.sniiv.modelos.EstadoMenu;
import mx.gob.conavi.sniiv.modelos.demanda.Financiamiento;
import mx.gob.conavi.sniiv.parsing.ParseFinanciamiento;
import mx.gob.conavi.sniiv.sqlite.FinanciamientoRepository;


public class FinanciamientosFragment extends DemandaBaseFragment {
    public static final String TAG = "EvolucionFragment";

    private DatosFinanciamiento datos;
    private ConsultaFinanciamiento entidad;
    private FinanciamientoRepository repository;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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

    protected void mostrarDatos() {
        if (entidad == null) {
            return;
        }

        inicializaDatos();

        if (configuracion.equals("sw600dp")) {
            createFragment();
        }

        if (mChart != null) {
            inicializaDatosChart();
        }
    }

    protected void createFragment() {
        DatosFinanciamientosDialogFragment dialog = (DatosFinanciamientosDialogFragment) getChildFragmentManager()
                .findFragmentById(R.id.datos);

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();

        if (dialog == null) {
            dialog = DatosFinanciamientosDialogFragment.newInstance(titulo, entidad.getAcciones(),
                    entidad.getMontos());

            transaction.add(R.id.datos, dialog);
            transaction.addToBackStack(null);
        } else {
            dialog.actualizaDatos(titulo, entidad.getAcciones(), entidad.getMontos());
            transaction.detach(dialog).attach(dialog);
        }

        transaction.commit();
    }

    protected void intentaInicializarGrafica() {
        if (entidad == null) {
            estado = EnumSet.of(EstadoMenu.NINGUNO);
            return;
        }

        if (configuracion.equals("sw600dp")) {
            inicializaDatosChart();
            estado = EnumSet.of(EstadoMenu.GUARDAR);
        } else {
            estado = EstadoMenu.AMBOS;
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

                idEntidad = newVal;

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

                saveTimeLastUpdated(getFechaActualizacion().getTime());

                loadFechasStorage();
            } catch (Exception e) {
                Log.v(TAG, "Error obteniendo datos");
                errorRetrievingData = true;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void s) {
            Log.v(TAG, "onPostExecute: errorRetrievingData " + errorRetrievingData );
            if (!errorRetrievingData) {
                habilitaPantalla();
                intentaInicializarGrafica();
                getActivity().invalidateOptionsMenu();
            } else {
                Utils.alertDialogShow(getActivity(), getString(R.string.mensaje_error_datos));
                progressDialog.dismiss();
            }
        }
    }

    protected void inicializaDatosChart() {
        ArrayList<String> pParties =  entidad.getParties();
        long[] pValues = entidad.getValues();
        String pCenterText = "Financiamientos";
        PieChartBuilder.buildPieChart(mChart, pParties, pValues, pCenterText,
                idEntidad, getString(R.string.etiqueta_conavi));
        OnChartValueSelectedMillones listener = new OnChartValueSelectedMillones(mChart, getKey(), idEntidad, pParties);
        mChart.setOnChartValueSelectedListener(listener);
    }

    protected void inicializaDatos() {
        if (fechas != null) {
            String financiamiento = String.format("%s (%s)", getString(R.string.title_financiamiento),
                    fechas.getFecha_finan_ui());
            titulo =  financiamiento;
        } else {
            titulo = getString(R.string.title_financiamiento);
        }
    }

    protected void muestraDialogo() {
        DatosFinanciamientosDialogFragment dialog =
                DatosFinanciamientosDialogFragment.newInstance(titulo, entidad.getAcciones(),
                        entidad.getMontos());
        dialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.MyDialogTheme );
        dialog.show(getFragmentManager(), "DatosOfertaDialog");
    }
}
