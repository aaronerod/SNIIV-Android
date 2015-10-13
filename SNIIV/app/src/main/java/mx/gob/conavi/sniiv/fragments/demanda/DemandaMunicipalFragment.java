package mx.gob.conavi.sniiv.fragments.demanda;

import android.app.ProgressDialog;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;

import java.util.ArrayList;

import butterknife.Bind;
import mx.gob.conavi.sniiv.R;
import mx.gob.conavi.sniiv.Utils.Utils;
import mx.gob.conavi.sniiv.charts.BarChartConfig;
import mx.gob.conavi.sniiv.charts.StackedBarChartBuilder;
import mx.gob.conavi.sniiv.datos.Datos;
import mx.gob.conavi.sniiv.datos.DatosDemandaMunicipal;
import mx.gob.conavi.sniiv.fragments.BaseFragment;
import mx.gob.conavi.sniiv.modelos.DemandaMunicipalTipo;
import mx.gob.conavi.sniiv.modelos.Evolucion;
import mx.gob.conavi.sniiv.modelos.demanda.DemandaMunicipal;
import mx.gob.conavi.sniiv.parsing.ParseDemandaMunicipal;
import mx.gob.conavi.sniiv.sqlite.DemandaMunicipalRepository;

/**
 * Created by octavio.munguia on 08/10/2015.
 */
public class DemandaMunicipalFragment extends BaseFragment<DemandaMunicipal> {
    private static final String TAG = DemandaMunicipalFragment.class.getSimpleName();
    @Nullable @Bind(R.id.chart) BarChart mChart;

    protected String titulo;
    private boolean errorRetrievingData = false;
    private boolean showAcciones = true;
    private Menu menu;
    private DemandaMunicipalTipo tipo;

    public static DemandaMunicipalFragment newInstance(DemandaMunicipalTipo tipo) {
        DemandaMunicipalFragment myFragment = new DemandaMunicipalFragment();

        Bundle args = new Bundle();
        args.putInt("tipo", tipo.getValue());
        myFragment.setArguments(args);

        return myFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        tipo = DemandaMunicipalTipo.fromInteger(getArguments().getInt(getString(R.string.etiqueta_tipo), 0));
        repository = new DemandaMunicipalRepository(getActivity(), tipo);
    }

    @Override
    public void onResume() {
        super.onResume();
        pickerEstados.setValue(1);
    }

    @Override
    public void onPause() {
        super.onPause();
        errorShowed = false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        if (mChart != null) {
            mChart.setNoDataText(getString(R.string.mensaje_datos_no_disponibles));
        }

        return view;
    }

    protected NumberPicker.OnValueChangeListener configuraValueChangeListener() {
        return new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                if (newVal == 0) {
                    entidad = datos.consultaNacional();
                } else {
                    entidad = datos.consultaEntidad(newVal - 1);
                }

                mostrarDatos();
            }
        };
    }

    /*protected void configuraPickerView() {
        pickerEstados.setMaxValue(Utils.listEdo.length - 1);
        pickerEstados.setMinValue(1);
        pickerEstados.setDisplayedValues(Utils.listEdoNoNacional);
    }*/

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_evolucion, menu);
        this.menu = menu;

        if (entidad == null) {
            return;
        }

        menu.findItem(R.id.action_guardar).setVisible(true);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_toggle:
                showAcciones = !showAcciones;
                setOptionTitle();
                inicializaDatosChart();
                break;
            case R.id.action_guardar:
                guardarChart();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_demanda_municipal;
    }

    @Override
    protected void mostrarDatos() {
        if (entidad == null) {
            return;
        }

        inicializaDatos();

        if (mChart != null) {
            inicializaDatosChart();
        }
    }

    protected void guardarChart() {
        if (mChart != null) {
            mChart.saveToGallery(getKey() + System.currentTimeMillis() + ".jpg", 100);
            Toast.makeText(getActivity().getApplicationContext(),
                    R.string.mensaje_imagen_guardada, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected AsyncTask<Void, Void, Void> getAsyncTask() {
        return new AsyncTaskRunner();
    }

    @Override
    protected String getKey() {
        switch (tipo) {
            case FINANCIAMIENTOS:
                return DemandaMunicipal.FINANCIAMIENTO;
            case SUBSIDIOS:
                return DemandaMunicipal.SUBSIDIO;
            default:
                throw new IllegalArgumentException(getString(R.string.etiqueta_tipo));
        }
    }

    @Override
    protected String getFechaAsString() {
        return fechas != null ? getFecha() : null;
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
                ParseDemandaMunicipal parse = new ParseDemandaMunicipal(tipo);
                DemandaMunicipal[] datosParse = parse.getDatos();
                repository.deleteAll();
                // ((DemandaMunicipalRepository)repository).saveAll(parse.getJsonObject());

                datos = new DatosDemandaMunicipal(datosParse);
                entidad = datos.consultaEntidad(0);

                //saveTimeLastUpdated(getFechaActualizacion().getTime());

                loadFechasStorage();
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
                getActivity().invalidateOptionsMenu();
            } else {
                Utils.alertDialogShow(getActivity(), getString(R.string.mensaje_error_datos));
                progressDialog.dismiss();
            }
        }
    }

    protected void inicializaDatosChart() {
            BarChartConfig config = new BarChartConfig();
            config.setParties(entidad.getParties());
            config.setyValues(getYValues());
            config.setDescription(titulo);
            config.setxValues(getXValues());
            config.setShowAcciones(showAcciones);
            config.setConfiguracion(configuracion);
            StackedBarChartBuilder.buildLineChart(getActivity(), mChart, config);
    }

    protected void inicializaDatos() {
        if (fechas != null) {
            String finan = String.format("%s\n(%s)", getTitle(),
                    Utils.formatoDiaMes(getFecha()));
            titulo =  finan;
        } else {
            titulo = getTitle();
        }
    }

    protected Datos<DemandaMunicipal> getDatos(DemandaMunicipal[] datos) {
        return new DatosDemandaMunicipal(datos);
    }

    private ArrayList<float[]> getYValues() {
        if (showAcciones) {
            return entidad.getYValuesAcciones();
        } else {
            return entidad.getYValuesMontos();
        }
    }

    private ArrayList<String> getXValues() {
        if (showAcciones) {
            return entidad.getXValuesAcciones();
        } else {
            return entidad.getXValuesMontos();
        }
    }

    private void setOptionTitle() {
        MenuItem item = menu.findItem(R.id.action_toggle);
        if (showAcciones){
            item.setTitle(R.string.title_menu_montos);
        } else {
            item.setTitle(R.string.title_menu_acciones);
        }
    }

    private String getFecha() {
        switch (tipo) {
            case FINANCIAMIENTOS:
                return fechas.getFecha_finan();
            case SUBSIDIOS:
                return fechas.getFecha_subs();
            default:
                throw new IllegalArgumentException(getString(R.string.etiqueta_tipo));
        }
    }

    private String getTitle() {
        Resources res = getResources();
        switch (tipo) {
            // Los dos casos realizan la misma acción
            case FINANCIAMIENTOS:
            case SUBSIDIOS:
                return String.format(res.getString(R.string.title_otorgados), getKey(), getNombreEntidad());
            default:
                throw new IllegalArgumentException(getString(R.string.etiqueta_tipo));
        }
    }

    private String getNombreEntidad() {
        int numEntidad = pickerEstados.getValue();
        if (configuracion.equals("sw600dp")) {
            return Utils.listEdo[numEntidad];
        } else {
            return Utils.ENTIDAD_ABR[numEntidad];
        }
    }

    @Override
    protected DemandaMunicipal consulta(Datos<DemandaMunicipal> datos) {
        return datos.consultaEntidad(1);
    }
}
