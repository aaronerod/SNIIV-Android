package mx.gob.conavi.sniiv.fragments.evolucion;

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
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;

import java.util.ArrayList;

import butterknife.Bind;
import mx.gob.conavi.sniiv.R;
import mx.gob.conavi.sniiv.Utils.Utils;
import mx.gob.conavi.sniiv.charts.LineChartBuilder;
import mx.gob.conavi.sniiv.charts.LineChartConfig;
import mx.gob.conavi.sniiv.datos.Datos;
import mx.gob.conavi.sniiv.datos.DatosEvolucion;
import mx.gob.conavi.sniiv.fragments.BaseFragment;
import mx.gob.conavi.sniiv.modelos.Evolucion;
import mx.gob.conavi.sniiv.modelos.EvolucionTipo;
import mx.gob.conavi.sniiv.parsing.ParseEvolucion;
import mx.gob.conavi.sniiv.sqlite.EvolucionRepository;

/**
 * Created by octavio.munguia on 28/09/2015.
 */
public class EvolucionFragment extends BaseFragment<Evolucion> {
    private static final String TAG = EvolucionFragment.class.getSimpleName();
    @Nullable  @Bind(R.id.chart) LineChart mChart;

    protected String titulo;
    private boolean errorRetrievingData = false;
    private boolean showAcciones = true;
    private Menu menu;
    private EvolucionTipo tipo;

    public static EvolucionFragment newInstance(EvolucionTipo tipo) {
        EvolucionFragment myFragment = new EvolucionFragment();

        Bundle args = new Bundle();
        args.putInt("tipo", tipo.getValue());
        myFragment.setArguments(args);

        return myFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        tipo = EvolucionTipo.fromInteger(getArguments().getInt(getString(R.string.etiqueta_tipo), 0));
        repository = new EvolucionRepository(getActivity(), tipo);
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

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_evolucion, menu);
        this.menu = menu;

        if (entidad == null) {
            return;
        }

        menu.findItem(R.id.action_guardar).setVisible(true);
        if (tipo != EvolucionTipo.REGISTRO_VIVIENDA) {
            menu.findItem(R.id.action_toggle).setVisible(true);
        }

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
        return R.layout.fragment_evolucion;
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
                return Evolucion.FINANCIAMIENTO;
            case SUBSIDIOS:
                return Evolucion.SUBSIDIO;
            case REGISTRO_VIVIENDA:
                return Evolucion.REGISTRO_VIVIENDA;
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
                ParseEvolucion parse = new ParseEvolucion(tipo);
                Evolucion[] datosParse = parse.getDatos();
                repository.deleteAll();
                ((EvolucionRepository)repository).saveAll(parse.getJsonObject());

                datos = new DatosEvolucion(datosParse);
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
        int[] xValues = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
        LineChartConfig config = new LineChartConfig();
        config.setParties(entidad.getParties());
        config.setyValues(getValues());
        config.setDescription(titulo);
        config.setxValues(xValues);
        config.setShowAcciones(showAcciones);
        config.setConfiguracion(configuracion);
        LineChartBuilder.buildLineChart(getActivity(), mChart,config);
    }

    protected void inicializaDatos() {
        if (fechas != null) {
            String finan = String.format("%s\n(%s)", getTitle(), getFechaUI());
            titulo =  finan;
        } else {
            titulo = getTitle();
        }
    }

    protected Datos<Evolucion> getDatos(Evolucion[] datos) {
        return new DatosEvolucion(datos);
    }

    private ArrayList<double[]> getValues() {
        if (showAcciones) {
            return entidad.getYValuesAcciones();
        } else {
            return entidad.getYValuesMontos();
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
            case REGISTRO_VIVIENDA:
                return fechas.getFecha_vv();
            default:
                throw new IllegalArgumentException(getString(R.string.etiqueta_tipo));
        }
    }

    private String getFechaUI() {
        switch (tipo) {
            case FINANCIAMIENTOS:
                return fechas.getFecha_finan_ui();
            case SUBSIDIOS:
                return fechas.getFecha_subs_ui();
            case REGISTRO_VIVIENDA:
                return fechas.getFecha_vv_ui();
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
            case REGISTRO_VIVIENDA:
                return getString(R.string.title_inventario_oferta);
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
}