package mx.gob.conavi.sniiv.fragments;


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
import mx.gob.conavi.sniiv.datos.DatosSubsidio;
import mx.gob.conavi.sniiv.listeners.OnChartValueSelected;
import mx.gob.conavi.sniiv.listeners.OnChartValueSelectedMillones;
import mx.gob.conavi.sniiv.modelos.ConsultaSubsidio;
import mx.gob.conavi.sniiv.modelos.EstadoMenu;
import mx.gob.conavi.sniiv.modelos.Subsidio;
import mx.gob.conavi.sniiv.parsing.ParseSubsidio;
import mx.gob.conavi.sniiv.sqlite.SubsidioRepository;

public class SubsidiosFragment extends DemandaBaseFragment {
    public static final String TAG = "SubsidiosFragment";

    private DatosSubsidio datos;
    private ConsultaSubsidio entidad;
    private SubsidioRepository repository;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        repository = new SubsidioRepository(getActivity());
        valueChangeListener = configuraValueChangeListener();
    }

    protected void loadFromStorage() {
        Subsidio[] datosStorage = repository.loadFromStorage();
        if(datosStorage.length > 0) {
            datos = new DatosSubsidio(getActivity(), datosStorage);
            entidad = datos.consultaNacional();
            pickerEstados.setEnabled(true);
        } /*else {
            Utils.alertDialogShow(getActivity(), getString(R.string.no_conectado));
        } */

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
        DatosSubsidiosDialogFragment dialog = (DatosSubsidiosDialogFragment) getChildFragmentManager()
                .findFragmentById(R.id.datos);

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();

        if (dialog == null) {
            dialog = DatosSubsidiosDialogFragment.newInstance(titulo, entidad.getAcciones(),
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
        return Subsidio.TABLE;
    }

    @Override
    protected String getFechaAsString() {
        return fechas != null ? fechas.getFecha_subs() : null;
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

    protected void inicializaDatosChart() {
        ArrayList<String> pParties =  entidad.getParties();
        long[] pValues = entidad.getValues();
        String pCenterText = "Subsidios";
        PieChartBuilder.buildPieChart(mChart, pParties, pValues, pCenterText,
                idEntidad, getString(R.string.etiqueta_conavi));
        OnChartValueSelected listener = new OnChartValueSelectedMillones(mChart, getKey(), idEntidad, pParties);
        mChart.setOnChartValueSelectedListener(listener);
    }

    protected void inicializaDatos() {
        if (fechas != null) {
            String subsidio = String.format("%s (%s)", getString(R.string.title_subsidios),
                    Utils.formatoMes(fechas.getFecha_subs()));
            titulo =  subsidio;
        } else {
            titulo = getString(R.string.title_subsidios);
        }
    }

    protected void muestraDialogo() {
        DatosSubsidiosDialogFragment dialog =
                DatosSubsidiosDialogFragment.newInstance(titulo, entidad.getAcciones(),
                        entidad.getMontos());
        dialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.MyDialogTheme );
        dialog.show(getFragmentManager(), "DatosSubsidiosDialog");
    }
}
