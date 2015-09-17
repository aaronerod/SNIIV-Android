package mx.gob.conavi.sniiv.fragments;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;

import java.util.ArrayList;
import java.util.EnumSet;

import butterknife.Bind;
import butterknife.ButterKnife;
import mx.gob.conavi.sniiv.R;
import mx.gob.conavi.sniiv.Utils.Utils;
import mx.gob.conavi.sniiv.charts.PieChartBuilder;
import mx.gob.conavi.sniiv.datos.DatosFinanciamiento;
import mx.gob.conavi.sniiv.listeners.OnChartValueSelected;
import mx.gob.conavi.sniiv.modelos.ConsultaFinanciamiento;
import mx.gob.conavi.sniiv.modelos.EstadoMenuOferta;
import mx.gob.conavi.sniiv.modelos.Financiamiento;
import mx.gob.conavi.sniiv.parsing.ParseFinanciamiento;
import mx.gob.conavi.sniiv.sqlite.FinanciamientoRepository;


public class FinanciamientosFragment extends BaseFragment {
    public static final String TAG = "FinanciamientosFragment";

    private DatosFinanciamiento datos;
    private ConsultaFinanciamiento entidad;
    private FinanciamientoRepository repository;
    protected EnumSet<EstadoMenuOferta> estado = EnumSet.of(EstadoMenuOferta.NINGUNO);
    protected int idEntidad = 0;
    protected String titulo;

    @Nullable @Bind(R.id.chart) PieChart mChart;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        repository = new FinanciamientoRepository(getActivity());
        valueChangeListener = configuraValueChangeListener();
    }

    @Override
    public void onResume() {
        super.onResume();

        intentaInicializarGrafica();
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

    private void createFragment() {
        DatosFinanciamientosDialogFragment fragment = new DatosFinanciamientosDialogFragment();
        FragmentTransaction transaction = getChildFragmentManager()
                .beginTransaction();

        transaction.add(R.id.datos, fragment)
                .addToBackStack(null).commit();
    }

    protected void intentaInicializarGrafica() {
        if (entidad == null) {
            estado = EnumSet.of(EstadoMenuOferta.NINGUNO);
            return;
        }
        if (configuracion.equals("sw600dp")) {
            inicializaDatosChart();
            estado = EnumSet.of(EstadoMenuOferta.GUARDAR);
        } else {
            estado = EstadoMenuOferta.AMBOS;
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

    protected void inicializaDatosChart() {
        ArrayList<String> pParties =  entidad.getParties();
        long[] pValues = entidad.getValues();
        String pCenterText = "Financiamientos";
        PieChartBuilder.buildPieChart(mChart, pParties, pValues, pCenterText,
                idEntidad, getString(R.string.etiqueta_conavi));
        OnChartValueSelected listener = new OnChartValueSelected(mChart, getKey(), idEntidad, pParties);
        mChart.setOnChartValueSelectedListener(listener);
    }

    protected void inicializaDatos() {
        if (fechas != null) {
            String financiamiento = String.format("%s (%s)", getString(R.string.title_financiamiento),
                    Utils.formatoMes(fechas.getFecha_finan()));
            titulo =  financiamiento;
        } else {
            titulo = getString(R.string.title_financiamiento);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_oferta, menu);

        MenuItem guardar = menu.findItem(R.id.action_guardar);
        MenuItem datos = menu.findItem(R.id.action_datos);

        if (estado.contains(EstadoMenuOferta.NINGUNO)) {
            guardar.setVisible(false);
            datos.setVisible(false);
        }

        if (estado.contains(EstadoMenuOferta.GUARDAR)){
            guardar.setVisible(true);
        }

        if (estado.contains(EstadoMenuOferta.DATOS)) {
            datos.setVisible(true);
        }

        datos.setVisible(true); // debug

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_datos:
                muestraDialogo();
                break;
            case R.id.action_guardar:
                guardarChart();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void guardarChart() {
        if (mChart != null) {
            mChart.saveToGallery(getKey() + System.currentTimeMillis() + ".jpg", 100);
            Toast.makeText(getActivity(),
                    R.string.mensaje_imagen_guardada, Toast.LENGTH_SHORT).show();
        }
    }

    protected void muestraDialogo() {
        DatosFinanciamientosDialogFragment dialog = new DatosFinanciamientosDialogFragment();
        Bundle args = new Bundle();
        args.putString("Titulo", titulo);
        dialog.setArguments(args);
        dialog.show(getFragmentManager(), "DatosOfertaDialog");
    }
}
