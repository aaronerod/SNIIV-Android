package mx.gob.conavi.sniiv.fragments;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;

import java.util.ArrayList;
import java.util.EnumSet;

import butterknife.Bind;
import butterknife.ButterKnife;
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

public class SubsidiosFragment extends BaseFragment {
    public static final String TAG = "SubsidiosFragment";

    private DatosSubsidio datos;
    private ConsultaSubsidio entidad;
    private SubsidioRepository repository;

    protected EnumSet<EstadoMenu> estado = EnumSet.of(EstadoMenu.NINGUNO);
    protected int idEntidad = 0;
    protected String titulo;

    @Nullable @Bind(R.id.chart) PieChart mChart;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        repository = new SubsidioRepository(getActivity());
        valueChangeListener = configuraValueChangeListener();
    }

    @Override
    public void onResume() {
        super.onResume();

        intentaInicializarGrafica();
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
        //Todo: Revisar bug en conteo nacional vivienda nueva
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_oferta, menu);

        MenuItem guardar = menu.findItem(R.id.action_guardar);
        MenuItem datos = menu.findItem(R.id.action_datos);

        if (estado.contains(EstadoMenu.NINGUNO)) {
            guardar.setVisible(false);
            datos.setVisible(false);
        }

        if (estado.contains(EstadoMenu.GUARDAR)){
            guardar.setVisible(true);
        }

        if (estado.contains(EstadoMenu.DATOS)) {
            datos.setVisible(true);
        }

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
        DatosSubsidiosDialogFragment dialog =
                DatosSubsidiosDialogFragment.newInstance(titulo, entidad.getAcciones(),
                        entidad.getMontos());
        dialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.MyDialogTheme );
        dialog.show(getFragmentManager(), "DatosSubsidiosDialog");
    }
}
